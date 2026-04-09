package org.demianov.auth.main.core.exceptions.models.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserEmailAlreadyTakenExceptionTest {

    @Test
    @DisplayName("Should initiate with correct email message")
    void create_Email_Success() {
        String email = "test@demianov.org";
        UserEmailAlreadyTakenException exception = new UserEmailAlreadyTakenException(email);

        assertAll(
                () -> assertThat(exception.getMessage())
                        .isEqualTo("The account of user with email " + email + " is already taken.")
        );
    }
}

