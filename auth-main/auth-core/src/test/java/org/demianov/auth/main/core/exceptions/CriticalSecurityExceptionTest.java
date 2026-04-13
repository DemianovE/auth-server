package org.demianov.auth.main.core.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CriticalSecurityExceptionTest {

    @Test
    @DisplayName("Should initiate with correct message and cause")
    void create_Success() {
        String message = "test";

        CriticalSecurityException exception = new CriticalSecurityException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }
}
