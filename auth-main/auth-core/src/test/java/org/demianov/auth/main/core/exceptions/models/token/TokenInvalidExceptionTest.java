package org.demianov.auth.main.core.exceptions.models.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TokenInvalidExceptionTest {

    @Test
    @DisplayName("Should initiate with correct message")
    void create_Success() {
        String message = "test";
        TokenInvalidException exception = new TokenInvalidException(message);

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo(message)
        );
    }

    @Test
    @DisplayName("Should initiate with correct default message")
    void create_noMessage_Success() {
        TokenInvalidException exception = new TokenInvalidException();

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo("Token is invalid or expired.")
        );
    }
}
