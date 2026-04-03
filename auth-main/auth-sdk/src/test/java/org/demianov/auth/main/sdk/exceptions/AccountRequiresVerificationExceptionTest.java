package org.demianov.auth.main.sdk.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AccountRequiresVerificationExceptionTest {

    @Test
    @DisplayName("Should initiate with correct status and tag")
    void create_Success() {
        AccountRequiresVerificationException exception = new AccountRequiresVerificationException();

        assertAll(
                () -> assertThat(exception.getMessage()).isEqualTo("User is not verified."),
                () -> assertThat(exception.getTag()).isEqualTo("FORBIDDEN"),
                () -> assertThat(exception.getStatus()).isEqualTo(403)
        );
    }
}
