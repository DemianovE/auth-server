package org.demianov.auth.main.sdk.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class InvalidCredentialsExceptionTest {

    @Test
    @DisplayName("Should initiate with correct status and tag")
    void create_Success() {
        InvalidCredentialsException exception = new InvalidCredentialsException();

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo("Provided email or password are incorrect"),
                () -> assertThat(exception.getTag()).isEqualTo("UNAUTHORIZED"),
                () -> assertThat(exception.getStatus()).isEqualTo(401)
        );
    }
}
