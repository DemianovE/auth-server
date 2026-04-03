package org.demianov.auth.main.sdk.dto.representation;

import org.demianov.auth.main.kernel.domain.models.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link UserAccountDTO} ensuring data integrity and validation rules.
 */
public class UserAccountDTOTest {
    private final UUID id = UUID.randomUUID();
    private final String email = "test@demianov.org";
    private final UserStatus status = UserStatus.ACTIVE;

    @Test
    @DisplayName("Should preserve data integrity")
    void create_Success() {
        UserAccountDTO dto = new UserAccountDTO(this.id, this.email, this.status);

        assertThat(dto.userId()).isEqualTo(this.id);
        assertThat(dto.email()).isEqualTo(this.email);
        assertThat(dto.status()).isEqualTo(this.status);
    }

    @Test
    @DisplayName("Should  throw NullPointerException on null values")
    void create_NullPointerException() {
        assertThatThrownBy(() -> new UserAccountDTO(null, this.email, this.status))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("userId cannot be null");

        assertThatThrownBy(() -> new UserAccountDTO(this.id, null, this.status))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("email cannot be null");

        assertThatThrownBy(() -> new UserAccountDTO(this.id, this.email, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("status cannot be null");
    }
}
