package org.demianov.auth.main.kernel.application.models;

import org.demianov.auth.main.kernel.domain.models.Email;
import org.demianov.auth.main.kernel.domain.models.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link UserSummary} ensuring data integrity and validation rules.
 */
public class UserSummaryTest {

    private final UUID id = UUID.randomUUID();
    private final Email email = new Email("test@email.com");
    private final UserStatus status = UserStatus.ACTIVE;

    @Test
    @DisplayName("Should preserve data integrity")
    void create_Success() {
        // Given
        UserSummary summary = new UserSummary(this.id, this.email, this.status);

        // Then
        assertThat(summary.id()).isEqualTo(this.id);
        assertThat(summary.email()).isEqualTo(this.email);
        assertThat(summary.status()).isEqualTo(this.status);
    }

    @Test
    @DisplayName("Should throw NullPointerException on null values")
    void create_NullPointerException() {
        assertThatThrownBy(() -> new UserSummary(null, this.email, this.status))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("User id");

        assertThatThrownBy(() -> new UserSummary(this.id, null, this.status))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("User email");

        assertThatThrownBy(() -> new UserSummary(this.id, this.email, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("User status");
    }
}
