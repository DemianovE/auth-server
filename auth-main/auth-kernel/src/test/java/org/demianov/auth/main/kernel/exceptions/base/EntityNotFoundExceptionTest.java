package org.demianov.auth.main.kernel.exceptions.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class EntityNotFoundExceptionTest {

    @Test
    @DisplayName("Should initiate with correct status and tag")
    void create_Success() {
        String message = "test";
        EntityNotFoundException exception = new EntityNotFoundException(message);

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo(message),
                () -> assertThat(exception.getTag()).isEqualTo("NOT_FOUND"),
                () -> assertThat(exception.getStatus()).isEqualTo(404)
        );
    }
}
