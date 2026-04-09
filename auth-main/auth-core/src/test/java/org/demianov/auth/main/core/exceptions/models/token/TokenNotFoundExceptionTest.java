package org.demianov.auth.main.core.exceptions.models.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TokenNotFoundExceptionTest {

    @Test
    @DisplayName("Should initiate with correct default message")
    void create_noMessage_Success() {
        TokenNotFoundException exception = new TokenNotFoundException();

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo("Token not missing or non existing.")
        );
    }
}
