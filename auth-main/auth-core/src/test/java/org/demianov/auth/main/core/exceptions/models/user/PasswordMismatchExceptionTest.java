package org.demianov.auth.main.core.exceptions.models.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PasswordMismatchExceptionTest {

    @Test
    @DisplayName("Should initiate with correct default message")
    void create_noMessage_Success() {
        PasswordMismatchException exception = new PasswordMismatchException();

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo("Password is incorrect.")
        );
    }
}
