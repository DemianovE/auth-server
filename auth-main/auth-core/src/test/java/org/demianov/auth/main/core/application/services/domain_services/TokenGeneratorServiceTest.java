package org.demianov.auth.main.core.application.services.domain_services;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.ports.out.persistence.RefreshTokenRepoPort;
import org.demianov.auth.main.core.application.ports.out.persistence.UserRepoPort;
import org.demianov.auth.main.core.application.ports.out.security.SecureStringGeneratorPort;
import org.demianov.auth.main.core.application.ports.out.security.TokenInspectorPort;
import org.demianov.auth.main.core.domain.models.RefreshToken;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.DataAccessException;
import org.demianov.auth.main.core.exceptions.models.token.TokenNotFoundException;
import org.demianov.auth.main.core.exceptions.models.user.UserNotFoundException;
import org.demianov.auth.main.core.exceptions.ports.SecureStringGeneratorPortException;
import org.demianov.auth.main.core.exceptions.ports.TokenInspectorPortException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenGeneratorServiceTest {
    @Mock private SecureStringGeneratorPort  secureStringGenerator;
    @Mock private TokenInspectorPort tokenInspector;
    @Mock private RefreshTokenRepoPort refreshTokenRepo;
    @Mock private UserRepoPort userRepo;

    private TokenGeneratorService service;
    private final Duration accessTtl = Duration.ofMinutes(15);
    private final Duration refreshTtl = Duration.ofDays(7);
    private final Instant fixedNow = Instant.parse("2026-03-26T10:00:00Z");

    @BeforeEach
    void SetUp(){
        Clock fixedClock = Clock.fixed(this.fixedNow, ZoneId.of("UTC"));
        this.service = new TokenGeneratorService(
                this.secureStringGenerator, this.tokenInspector, this.refreshTokenRepo,
                this.userRepo, fixedClock, this.refreshTtl, this.accessTtl
        );
    }

    @Test
    @DisplayName("Should generate new token and save refresh token to repo")
    void generate_Success() {
        User user = mock(User.class);
        UUID userId = UUID.randomUUID();

        when(user.getId()).thenReturn(userId);

        String mockAccessToken = "access-token-123";
        String mockSecureString = "secure-random-string";

        when(this.tokenInspector.generateAccessToken(user)).thenReturn(mockAccessToken);
        when(this.secureStringGenerator.generate()).thenReturn(mockSecureString);

        LoginResult result = this.service.generate(user);

        assertThat(result).isInstanceOf(LoginResult.Success.class)
                        .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Success.class))
                                .satisfies(success -> {
                                    assertThat(success.accessToken()).isEqualTo(mockAccessToken);
                                    assertThat(success.refreshToken()).isEqualTo(mockSecureString);
                                    assertThat(success.refreshTokenTtl()).isEqualTo(this.refreshTtl);
                                    assertThat(success.expiresIn()).isEqualTo(this.accessTtl);
                                });


        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(this.refreshTokenRepo).save(captor.capture());

        RefreshToken savedToken = captor.getValue();
        assertThat(savedToken.userId()).isEqualTo(userId);
        assertThat(savedToken.token()).isEqualTo(mockSecureString);
        assertThat(savedToken.expiry()).isEqualTo(this.fixedNow.plus(this.refreshTtl));
    }

    @Test
    @DisplayName("Should return wrapped DataAccessException")
    void generate_DataAccessException() {
        User user = mock(User.class);

        String message = "Database error";
        Throwable cause = new RuntimeException(message);
        when(this.tokenInspector.generateAccessToken(user)).thenThrow(new DataAccessException(message, cause));

        LoginResult result = this.service.generate(user);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Failure.class))
                .satisfies(failure -> assertThat(failure.exception()).isInstanceOf(DataAccessException.class)
                        .asInstanceOf(InstanceOfAssertFactories.type(DataAccessException.class))
                        .satisfies(dataAccessException -> {
                            assertThat(dataAccessException.getMessage()).isEqualTo(message);
                            assertThat(dataAccessException.getCause()).isEqualTo(cause);
                            assertThat(dataAccessException.getCause().getMessage()).isEqualTo(cause.getMessage());
                        }));
    }

    @Test
    @DisplayName("Should return wrapped TokenInspectorPortException")
    void generate_TokenInspectorPortException() {
        User user = mock(User.class);

        String message = "Database error";
        Throwable cause = new RuntimeException(message);
        when(this.tokenInspector.generateAccessToken(user)).thenThrow(new TokenInspectorPortException(message, cause));

        LoginResult result = this.service.generate(user);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Failure.class))
                .satisfies(failure -> assertThat(failure.exception()).isInstanceOf(TokenInspectorPortException.class)
                        .asInstanceOf(InstanceOfAssertFactories.type(TokenInspectorPortException.class))
                        .satisfies(dataAccessException -> {
                            assertThat(dataAccessException.getMessage()).isEqualTo(message);
                            assertThat(dataAccessException.getCause()).isEqualTo(cause);
                            assertThat(dataAccessException.getCause().getMessage()).isEqualTo(cause.getMessage());
                        }));
    }

    @Test
    @DisplayName("Should return wrapped SecureStringGeneratorPortException")
    void generate_SecureStringGeneratorPortException() {
        User user = mock(User.class);

        String message = "Database error";
        Throwable cause = new RuntimeException(message);
        when(this.tokenInspector.generateAccessToken(user)).thenThrow(new SecureStringGeneratorPortException(message, cause));

        LoginResult result = this.service.generate(user);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Failure.class))
                .satisfies(failure -> assertThat(failure.exception()).isInstanceOf(SecureStringGeneratorPortException.class)
                        .asInstanceOf(InstanceOfAssertFactories.type(SecureStringGeneratorPortException.class))
                        .satisfies(dataAccessException -> {
                            assertThat(dataAccessException.getMessage()).isEqualTo(message);
                            assertThat(dataAccessException.getCause()).isEqualTo(cause);
                            assertThat(dataAccessException.getCause().getMessage()).isEqualTo(cause.getMessage());
                        }));
    }

    @Test
    @DisplayName("Should throw any not expected exception")
    void generate_UnexpectedException() {
        User user = mock(User.class);
        String message = "Database error";
        Throwable cause = new RuntimeException(message);
        when(this.tokenInspector.generateAccessToken(user)).thenThrow(new RuntimeException(message, cause));

        assertThatThrownBy(() -> this.service.generate(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(message);
    }

    @Test
    @DisplayName("Should rotate tokens successfully")
    void refresh_Success() {
        String oldTokenStr = "refresh-token-123";

        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";

        UUID userId = UUID.randomUUID();
        RefreshToken oldToken = new RefreshToken(UUID.randomUUID(), oldTokenStr, userId, this.fixedNow.plus(this.refreshTtl));

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        when(this.refreshTokenRepo.findByToken(oldTokenStr)).thenReturn(Optional.of(oldToken));
        when(this.userRepo.findById(userId)).thenReturn(Optional.of(user));

        when(this.tokenInspector.generateAccessToken(user)).thenReturn(newAccessToken);
        when(this.secureStringGenerator.generate()).thenReturn(newRefreshToken);

        LoginResult result = this.service.refresh(oldTokenStr);

        assertThat(result).isInstanceOf(LoginResult.Success.class)
                .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Success.class))
                .satisfies(success -> {
                    assertThat(success.accessToken()).isEqualTo(newAccessToken);
                    assertThat(success.refreshToken()).isEqualTo(newRefreshToken);
                    assertThat(success.refreshTokenTtl()).isEqualTo(this.refreshTtl);
                    assertThat(success.expiresIn()).isEqualTo(this.accessTtl);
                });

        verify(this.refreshTokenRepo, times(1)).delete(oldToken);
        verify(this.refreshTokenRepo, times(1)).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Should return wrapped TokenNotFoundException for invalid token")
    void refresh_TokenNotFoundException() {
        String invalidToken = "invalid-token";

        when(this.refreshTokenRepo.findByToken(invalidToken)).thenReturn(Optional.empty());

        LoginResult result = this.service.refresh(invalidToken);
        assertFailureContains(result, TokenNotFoundException.class);
    }

    @Test
    @DisplayName("Should return wrapped UserNotFoundException for invalid user")
    void refresh_UserNotFoundException() {
        String invalidToken = "invalid-token";
        UUID userId = UUID.randomUUID();

        RefreshToken token = new RefreshToken(UUID.randomUUID(), invalidToken, userId, this.fixedNow.plus(this.refreshTtl));
        when(this.refreshTokenRepo.findByToken(invalidToken)).thenReturn(Optional.of(token));

        when(this.userRepo.findById(userId)).thenReturn(Optional.empty());

        LoginResult result = this.service.refresh(invalidToken);
        assertFailureContains(result, UserNotFoundException.class);
    }

    /**
     * Perform a check that the result is a failure and contains the expected exception.
     * @param result the result to check.
     * @param exceptionClass the expected exception class.
     * @param <T> the type of the expected exception.
     */
    private <T extends Throwable> void assertFailureContains(LoginResult result, Class<T> exceptionClass) {
        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Failure.class))
                .satisfies(failure -> assertThat(failure.exception())
                        .as("Failure should contain an exception of type " + exceptionClass.getSimpleName())
                        .isInstanceOf(exceptionClass));
    }

}
