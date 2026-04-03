package org.demianov.auth.main.sdk.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link LoginRequest} ensuring data integrity and validation rules.
 */
public class LoginRequestTest {
    private final String email = "test@demianov.org";
    private final String password = "password";

    @Test
    @DisplayName("Should preserve data integrity")
    void create_Success() {
        LoginRequest dto = new LoginRequest(this.email, this.password);

        assertThat(dto.email()).isEqualTo(this.email);
        assertThat(dto.password()).isEqualTo(this.password);
    }

    @Test
    @DisplayName("Should  throw NullPointerException on null values")
    void create_NullPointerException() {
        assertThatThrownBy(() -> new LoginRequest(null, this.password))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("email cannot be null");

        assertThatThrownBy(() -> new LoginRequest(this.email, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("password cannot be null");
    }
}
