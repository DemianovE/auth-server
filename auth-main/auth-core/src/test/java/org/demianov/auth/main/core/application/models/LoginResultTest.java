package org.demianov.auth.main.core.application.models;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.demianov.auth.main.core.exceptions.models.user.PasswordMismatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LoginResultTest {

    private final String accessToken = "access-token";
    private final String refreshToken = "refresh-token";
    private final Duration accessTTL = Duration.ofMinutes(10);
    private final Duration refreshTTL = Duration.ofDays(1);

    @Test
    @DisplayName("Success should preserve data integrity")
    void create_Success_Success() {
        LoginResult dto = new LoginResult.Success(
                this.accessToken,
                this.refreshToken,
                this.accessTTL,
                this.refreshTTL
        );

        assertThat(dto).isInstanceOf(LoginResult.Success.class)
                        .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Success.class))
                                .satisfies(success -> {
                                    assertThat(success.accessToken()).isEqualTo(this.accessToken);
                                    assertThat(success.refreshToken()).isEqualTo(this.refreshToken);
                                    assertThat(success.refreshTokenTtl()).isEqualTo(this.refreshTTL);
                                    assertThat(success.expiresIn()).isEqualTo(this.accessTTL);
                                });
    }

    @Test
    @DisplayName("Failure should preserve data integrity")
    void create_Failure_Success() {
        LoginResult dto = new LoginResult.Failure(new PasswordMismatchException());

        assertThat(dto).isInstanceOf(LoginResult.Failure.class)
                .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Failure.class))
                .satisfies(failure -> assertThat(failure.exception()).isInstanceOf(PasswordMismatchException.class));
    }

    @Test
    @DisplayName("Should  throw NullPointerException on null values in Success")
    void create_Success_NullPointerException() {
        assertThatThrownBy(() -> new LoginResult.Success(null, this.refreshToken, this.accessTTL, this.refreshTTL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("accessToken cannot be null");

        assertThatThrownBy(() -> new LoginResult.Success(this.accessToken, null, this.accessTTL, this.refreshTTL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("refreshToken cannot be null");

        assertThatThrownBy(() -> new LoginResult.Success(this.accessToken, this.refreshToken, null, this.refreshTTL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("expiresIn cannot be null");

        assertThatThrownBy(() -> new LoginResult.Success(this.accessToken, this.refreshToken, this.accessTTL, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("refreshTokenTtl cannot be null");
    }

    @Test
    @DisplayName("Should return true for Success")
    void create_Failure_NullPointerException() {
        assertThatThrownBy(() -> new LoginResult.Failure(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("exception cannot be null");
    }
}
