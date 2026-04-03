package org.demianov.auth.main.kernel.exceptions.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthenticationFailureExceptionTest {

    @Test
    @DisplayName("Should initiate with correct status and tag")
    void create_Success() {
        String message = "test";
        AuthenticationFailureException exception = new AuthenticationFailureException(message);

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo(message),
                () -> assertThat(exception.getTag()).isEqualTo("UNAUTHORIZED"),
                () -> assertThat(exception.getStatus()).isEqualTo(401)
        );
    }
}
