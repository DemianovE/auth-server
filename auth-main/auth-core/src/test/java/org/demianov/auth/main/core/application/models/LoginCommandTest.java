package org.demianov.auth.main.core.application.models;

import org.demianov.auth.main.kernel.domain.models.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LoginCommandTest {
    private final Email email = Email.of("test@demianov.org");
    private final String password = "password";

    @Test
    @DisplayName("Success should preserve data integrity")
    void create_Success() {
        LoginCommand dto = new LoginCommand(
                this.email,
                this.password
        );

        assertThat(dto.email()).isEqualTo(this.email);
        assertThat(dto.password()).isEqualTo(this.password);
    }

    @Test
    @DisplayName("Should return true for Success")
    void create_Failure_NullPointerException() {
        assertThatThrownBy(() -> new LoginCommand(null, this.password))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("email cannot be null");

        assertThatThrownBy(() -> new LoginCommand(this.email, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("password cannot be null");
    }
}
