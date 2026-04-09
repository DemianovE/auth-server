package org.demianov.auth.main.core.application.services.use_case.login;

import org.demianov.auth.main.core.application.models.LoginCommand;
import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.ports.out.persistence.UserRepoPort;
import org.demianov.auth.main.core.application.ports.out.security.PasswordHasherPort;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.models.user.UserEmailAlreadyTakenException;
import org.demianov.auth.main.core.exceptions.models.user.UserNotFoundException;

import org.demianov.auth.main.kernel.domain.models.Email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.Optional;

import org.assertj.core.api.InstanceOfAssertFactories;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {

    @Mock private UserRepoPort userRepo;
    @Mock private PasswordHasherPort passwordHasher;
    @Mock private LoginDispatcher dispatcher;

    @InjectMocks private LoginUseCase useCase;

    private final Email email = new Email("max.musstermann@demianov.org");
    private final String rawPassword = "rawPassword";
    private final LoginCommand command = new LoginCommand(this.email, this.rawPassword);

    @Test
    @DisplayName("Should successfully logging and trigger dispatch event")
    void execute_Success() {
        User user = mock(User.class);

        when(this.userRepo.findByEmail(this.email)).thenReturn(Optional.of(user));

        doNothing().when(user).authenticate("rawPassword", this.passwordHasher);
        doNothing().when(this.userRepo).save(user);

        LoginResult.Success result = new LoginResult.Success(
                "access",
                "refresh",
                Duration.ofMinutes(1000),
                Duration.ofMinutes(1000)
        );

        when(this.dispatcher.dispatch(user)).thenReturn(result);

        LoginResult actual = this.useCase.execute(this.command);

        assertThat(actual).isEqualTo(result);
        verify(user, times(1)).authenticate("rawPassword", this.passwordHasher);
        verify(this.userRepo, times(1)).save(user);
        verify(this.dispatcher, times(1)).dispatch(user);
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
    @DisplayName("Should throw Exception if not in LoginPortExceptions group")
    void execute_Exception() {
        when(this.userRepo.findByEmail(this.email)).thenThrow(UserEmailAlreadyTakenException.class);

        assertThatThrownBy(() -> this.useCase.execute(this.command)).isInstanceOf(UserEmailAlreadyTakenException.class);
    }
}
