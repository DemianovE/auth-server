package org.demianov.auth.main.core.exceptions.models.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TokenExceptionTest {

    @Test
    @DisplayName("Should initiate with correct message")
    void create_Success() {
        String message = "test";
        TokenException exception = new TokenException(message);

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo(message)
        );
    }
}
