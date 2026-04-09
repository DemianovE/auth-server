package org.demianov.auth.main.core.exceptions.models.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserNotFoundExceptionTest {
    @Test
    @DisplayName("Should initiate with correct email message")
    void create_Email_Success() {
        String email = "test@demianov.org";
        UserNotFoundException exception = new UserNotFoundException(email);

        assertAll(
                () -> assertThat(exception.getMessage())
                        .isEqualTo("User with email " + email + " was not found in the domain.")
        );
    }

    @Test
    @DisplayName("Should initiate with correct uuid message")
    void create_UUID_Success() {
        UUID id = UUID.randomUUID();
        UserNotFoundException exception = new UserNotFoundException(id);

        assertAll(
                () -> assertThat(exception.getMessage())
                        .isEqualTo("User with id " + id + " was not found in the domain.")
        );
    }
}
