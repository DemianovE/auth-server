package org.demianov.auth.main.core.application.services.use_case.login;

import org.assertj.core.api.ThrowableAssert;
import org.demianov.auth.main.core.application.models.FailureStrategy;
import org.demianov.auth.main.core.application.models.LoginCommand;
import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.models.TokenPair;
import org.demianov.auth.main.core.application.ports.in.login.CriticalLoginHandler;
import org.demianov.auth.main.core.application.ports.in.login.LoginDispatcher;
import org.demianov.auth.main.core.application.ports.in.login.LoginInputPort;
import org.demianov.auth.main.core.application.ports.out.listeners.LoginListener;
import org.demianov.auth.main.core.application.ports.out.persistence.UserRepoPort;
import org.demianov.auth.main.core.application.ports.out.security.PasswordHasherPort;
import org.demianov.auth.main.core.application.ports.out.security.TokenGeneratorPort;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.CriticalSecurityException;
import org.demianov.auth.main.core.exceptions.models.user.UserEmailAlreadyTakenException;
import org.demianov.auth.main.core.exceptions.models.user.UserNotFoundException;

import org.demianov.auth.main.core.exceptions.ports.UnexpectedSystemException;
import org.demianov.auth.main.kernel.domain.models.Email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.InstanceOfAssertFactories;
import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.AssertionsKt.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {

    @Mock private UserRepoPort userRepo;
    @Mock private PasswordHasherPort passwordHasher;
    @Mock private LoginDispatcher dispatcher;
    @Mock private TokenGeneratorPort tokenGenerator;

    @Mock private CriticalLoginHandler criticalHandler1;
    @Mock private CriticalLoginHandler criticalHandler2;
    @Mock private LoginListener listener1;
    @Mock private LoginListener listener2;

    private final User user = mock(User.class);

    @InjectMocks private LoginUseCase useCase;

    private final Email email = Email.of("max.musstermann@demianov.org");
    private final String rawPassword = "rawPassword";
    private final LoginCommand command = new LoginCommand(this.email, this.rawPassword);

    @Test
    @DisplayName("Should successfully logging and trigger dispatch event")
    void execute_Success() {
        when(this.userRepo.findByEmail(this.email)).thenReturn(Optional.of(this.user));

        doNothing().when(this.user).authenticate("rawPassword", this.passwordHasher);
        doNothing().when(this.userRepo).save(user);

        TokenPair tokens = createTokenPair();

        LoginResult.Success result = new LoginResult.Success(
                tokens.accessToken(),
                tokens.refreshToken(),
                tokens.expiresIn(),
                tokens.refreshTokenTtl()
        );

        when(this.tokenGenerator.generate(this.user)).thenReturn(tokens);
        when(this.dispatcher.dispatch(result, this.user)).thenReturn(result);

        LoginResult actual = this.useCase.execute(this.command);

        assertThat(actual).isEqualTo(result);
        verify(this.user, times(1)).authenticate("rawPassword", this.passwordHasher);
        verify(this.userRepo, times(1)).save(this.user);
        verify(this.dispatcher, times(1)).dispatch(result, this.user);
    }

    @Test
    @DisplayName("Should run all listeners and critical handlers when successful")
    void execute_Success_Listeners() {
        TokenPair tokens = createTokenPair();

        this.useCase = createLoginInputPort();

        when(this.userRepo.findByEmail(this.email)).thenReturn(Optional.of(this.user));
        when(this.tokenGenerator.generate(this.user)).thenReturn(tokens);
        when(this.dispatcher.dispatch(any(), any())).thenReturn(new LoginResult.Success("access", "refresh", Duration.ofMinutes(1000), Duration.ofMinutes(1000)));

        this.useCase.execute(this.command);

        verify(this.listener1, times(1)).onSuccess(this.user);
        verify(this.listener2, times(1)).onSuccess(this.user);

        verify(this.criticalHandler1, times(1)).perform(this.user);
        verify(this.criticalHandler2, times(1)).perform(this.user);
    }

    @Test
    @DisplayName("Should abort on failed Critical Handler and execute fail listeners")
    void execute_CriticalHandlerFailure() {
        this.useCase = createLoginInputPort();

        when(this.userRepo.findByEmail(this.email)).thenReturn(Optional.of(this.user));
        doThrow(new CriticalSecurityException("error")).when(this.criticalHandler1).perform(this.user);

        LoginResult result = this.useCase.execute(this.command);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .extracting(r -> ((LoginResult.Failure) r).exception())
                .asInstanceOf(InstanceOfAssertFactories.type(CriticalSecurityException.class))
                .satisfies(failure -> assertThat(failure.getMessage()).isEqualTo("error"));

        verify(this.criticalHandler1, times(1)).perform(this.user);
        verify(this.criticalHandler2, never()).perform(this.user);

        verify(this.listener1, never()).onSuccess(this.user);
        verify(this.listener2, times(0)).onSuccess(this.user);

        verify(this.listener1, times(1)).onFailure(this.command);
        verify(this.listener2, times(1)).onFailure(this.command);
    }

    @Test
    @DisplayName("Should throw no error if the listener throws error onSuccess()")
    void execute_ListenerFailure() {
        TokenPair tokens = createTokenPair();
        this.useCase = createLoginInputPort();

        when(this.userRepo.findByEmail(this.email)).thenReturn(Optional.of(this.user));
        when(this.tokenGenerator.generate(this.user)).thenReturn(tokens);
        when(this.dispatcher.dispatch(any(), any())).thenReturn(new LoginResult.Success("access", "refresh", Duration.ofMinutes(1000), Duration.ofMinutes(1000)));

        doThrow(new RuntimeException("error")).when(this.listener1).onSuccess(this.user);

        LoginResult result = this.useCase.execute(this.command);

        assertThat(result).isInstanceOf(LoginResult.Success.class);
        verify(this.listener1, times(1)).onSuccess(this.user);
        verify(this.listener2, times(1)).onSuccess(this.user);
    }

    @Test
    @DisplayName("Should throw no error if the listener throws error onFailure()")
    void execute_ListenerFailure_ListenerFailure() {
        this.useCase = createLoginInputPort();

        when(this.userRepo.findByEmail(this.email)).thenReturn(Optional.of(this.user));
        doThrow(new CriticalSecurityException("error")).when(this.criticalHandler1).perform(this.user);
        doThrow(new RuntimeException("error")).when(this.listener1).onFailure(this.command);

        LoginResult result = this.useCase.execute(this.command);

        assertThat(result).isInstanceOf(LoginResult.Failure.class);

        verify(this.criticalHandler1, times(1)).perform(this.user);

        verify(this.listener1, times(1)).onFailure(this.command);
        verify(this.listener2, times(1)).onFailure(this.command);
    }

    @Test
    @DisplayName("Should wrap error and abort when listener throws failure and FailureStrategy is FAST_FAIL")
    void execute_ListenerFailure_Failure() {
        this.useCase = createLoginInputPort(FailureStrategy.FAIL_FAST);
        when(this.userRepo.findByEmail(this.email)).thenReturn(Optional.of(this.user));

        doThrow(new CriticalSecurityException("error")).when(this.criticalHandler1).perform(this.user);
        doThrow(new RuntimeException("error")).when(this.listener1).onFailure(this.command);

        LoginResult result = this.useCase.execute(this.command);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .extracting(r -> ((LoginResult.Failure) r).exception())
                .isInstanceOf(CriticalSecurityException.class)
                .asInstanceOf(InstanceOfAssertFactories.type(CriticalSecurityException.class))
                .satisfies(failure -> assertThat(failure.getMessage()).isEqualTo("error"));

        verify(this.criticalHandler1).perform(this.user);

        verify(this.listener1).onFailure(this.command);
        verify(this.listener2, never()).onFailure(this.command);
    }

    @Test
    @DisplayName("Should return original domain error even if a listener explodes during failure handling")
    void handleFailure_ListenerExplodes_ReturnsOriginalError() {
        this.useCase = createLoginInputPort(FailureStrategy.FAIL_FAST);
        when(userRepo.findByEmail(any())).thenReturn(Optional.of(this.user));

        doThrow(new UserNotFoundException("Original Error")).when(criticalHandler1).perform(this.user);
        doThrow(new RuntimeException("Listener Error")).when(listener1).onFailure(command);

        LoginResult result = useCase.execute(command);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .extracting(r -> ((LoginResult.Failure) r).exception())
                .isInstanceOf(UserNotFoundException.class);

        verify(listener1).onFailure(command);
        verify(listener2, never()).onFailure(any());
    }

    @Test
    @DisplayName("Should return UnexpectedSystemException for any non AuthCoreException exception by CriticalHandler")
    void handleFailure_CriticalHandlerException_UnexpectedSystemException() {
        this.useCase = createLoginInputPort();
        when(userRepo.findByEmail(any())).thenReturn(Optional.of(this.user));

        doThrow(new RuntimeException("Original Error")).when(criticalHandler1).perform(this.user);

        LoginResult result = useCase.execute(command);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .extracting(r -> ((LoginResult.Failure) r).exception())
                .isInstanceOf(UnexpectedSystemException.class);

        verify(listener1, times(1)).onFailure(command);
        verify(listener2, times(1)).onFailure(command);
    }

    @Test
    @DisplayName("Should return AuthCoreException exception if thrown by CriticalHandler")
    void handleFailure_CriticalHandlerException_AuthCoreException() {
        this.useCase = createLoginInputPort();
        when(userRepo.findByEmail(any())).thenReturn(Optional.of(this.user));

        doThrow(new UserNotFoundException("Original Error")).when(criticalHandler1).perform(this.user);

        LoginResult result = useCase.execute(command);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .extracting(r -> ((LoginResult.Failure) r).exception())
                .isInstanceOf(UserNotFoundException.class)
                .asInstanceOf(InstanceOfAssertFactories.type(UserNotFoundException.class))
                .satisfies(failure -> assertThat(failure.getMessage()).contains("Original Error"));

        verify(listener1, times(1)).onFailure(command);
        verify(listener2, times(1)).onFailure(command);
    }

    @Test
    @DisplayName("Should ignore AuthCoreException when thrown by a standard listener in CONTINUE_ON_FAIL mode")
    void execute_ListenerThrowsAuthCoreException_IgnoredInContinueMode() {
        TokenPair tokens = createTokenPair();
        this.useCase = createLoginInputPort();

        when(this.userRepo.findByEmail(this.email)).thenReturn(Optional.of(this.user));
        when(this.tokenGenerator.generate(this.user)).thenReturn(tokens);
        when(this.dispatcher.dispatch(any(), any())).thenReturn(new LoginResult.Success("access", "refresh", Duration.ofMinutes(1000), Duration.ofMinutes(1000)));

        doThrow(new UserNotFoundException("Ignored Error"))
                .when(listener1).onSuccess(this.user);

        LoginResult result = useCase.execute(command);

        assertThat(result).isInstanceOf(LoginResult.Success.class);

        verify(listener1).onSuccess(this.user);
        verify(listener2).onSuccess(this.user);
    }

    @Test
    @DisplayName("Should return UserNotFoundException for false email")
    void execute_UserNotFoundException() {

        when(this.userRepo.findByEmail(this.email)).thenReturn(Optional.empty());

        LoginResult result = this.useCase.execute(this.command);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Failure.class))
                .satisfies(failure -> assertThat(failure.exception()).isInstanceOf(UserNotFoundException.class));

        verifyNoInteractions(this.dispatcher);
        verify(this.userRepo, times(1)).findByEmail(this.email);
    }

    @Test
    @DisplayName("All non AuthCore errors should be thrown")
    void execute_RuntimeException() {
        when(this.userRepo.findByEmail(this.email)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> this.useCase.execute(this.command)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should return wrapped Exception if not in LoginPortExceptions group")
    void execute_Exception() {
        when(this.userRepo.findByEmail(this.email)).thenThrow(UserEmailAlreadyTakenException.class);

        LoginResult result = this.useCase.execute(this.command);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .extracting(r -> ((LoginResult.Failure) r).exception())
                .isInstanceOf(UnexpectedSystemException.class)
                .asInstanceOf(InstanceOfAssertFactories.type(UnexpectedSystemException.class))
                .satisfies(exception -> assertThat(exception).hasCauseInstanceOf(UserEmailAlreadyTakenException.class));
    }

    private LoginUseCase createLoginInputPort() {
        return createLoginInputPort(FailureStrategy.CONTINUE_ON_FAIL);
    }

    private LoginUseCase  createLoginInputPort(final FailureStrategy strategy) {
        return new LoginUseCase(
                this.userRepo,
                this.passwordHasher,
                this.dispatcher,
                this.tokenGenerator,
                List.of(criticalHandler1, criticalHandler2),
                List.of(listener1, listener2),
                strategy);
    }

    private TokenPair createTokenPair() {
        return new TokenPair(
                "access",
                "refresh",
                Duration.ofMinutes(1000),
                Duration.ofMinutes(1000)
        );
    }
}
