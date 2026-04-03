package org.demianov.auth.main.sdk.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link AuthTokensDTO} ensuring data integrity and validation rules.
 */
public class AuthTokensDTOTest {

    @Test
    @DisplayName("Should preserve data integrity")
    void create_Success() {
        AuthTokensDTO dto = new AuthTokensDTO(
                "accessToken",
                "refreshToken",
                Duration.ofMinutes(15),
                Duration.ofDays(1));

        assertThat(dto.token()).isEqualTo("accessToken");
        assertThat(dto.refresh()).isEqualTo("refreshToken");
        assertThat(dto.expiresIn()).isEqualTo(Duration.ofMinutes(15));
        assertThat(dto.refreshTokenTtl()).isEqualTo(Duration.ofDays(1));
    }

    @Test
    @DisplayName("Should  throw NullPointerException on null values")
    void create_NullPointerException() {
        assertThatThrownBy(() -> new AuthTokensDTO(null, "refreshToken", Duration.ofMinutes(15), Duration.ofDays(1)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("token");

        assertThatThrownBy(() -> new AuthTokensDTO("accessToken", null, Duration.ofMinutes(15), Duration.ofDays(1)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("refresh");

        assertThatThrownBy(() -> new AuthTokensDTO("accessToken", "refreshToken", null, Duration.ofDays(1)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("expiresIn");

        assertThatThrownBy(() -> new AuthTokensDTO("accessToken", "refreshToken", Duration.ofMinutes(15), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("refreshTokenTtl");
    }
}
