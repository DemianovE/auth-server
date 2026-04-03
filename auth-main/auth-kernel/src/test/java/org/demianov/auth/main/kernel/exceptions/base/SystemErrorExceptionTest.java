package org.demianov.auth.main.kernel.exceptions.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SystemErrorExceptionTest {

    @Test
    @DisplayName("Should initiate with correct status and tag")
    void create_Success() {
        SystemErrorException exception = new SystemErrorException();

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo("An unexpected internal error occurred"),
                () -> assertThat(exception.getTag()).isEqualTo("INTERNAL_SERVER_ERROR"),
                () -> assertThat(exception.getStatus()).isEqualTo(500)
        );
    }
}
