package org.demianov.auth.main.core.exceptions.ports;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SecureStringGeneratorPortExceptionTest  {

    @Test
    @DisplayName("Should initiate with correct message and cause")
    void create_Success() {
        String message = "test";
        String causeMessage = "Port null pointer";
        Throwable cause = new RuntimeException(causeMessage);

        SecureStringGeneratorPortException exception = new SecureStringGeneratorPortException(message, cause);

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo(message),
                () -> assertThat(exception.getCause()).isEqualTo(cause),
                () -> assertThat(exception.getCause().getMessage()).isEqualTo(causeMessage)
        );
    }
}
