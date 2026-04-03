package org.demianov.auth.main.sdk.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TokenInvalidExceptionTest {

    @Test
    @DisplayName("Should initiate with correct status and tag")
    void create_Success() {
        TokenInvalidException exception = new TokenInvalidException();

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo("Token is invalid or expired."),
                () -> assertThat(exception.getTag()).isEqualTo("UNAUTHORIZED"),
                () -> assertThat(exception.getStatus()).isEqualTo(401)
        );
    }
}
