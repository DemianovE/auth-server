package org.demianov.auth.main.core.domain.models;

import org.demianov.auth.main.core.exceptions.models.token.TokenInvalidException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class RefreshTokenTest {

    private final UUID userId = UUID.randomUUID();
    private final String tokenValue = "some-secure-random-string-43-chars";

    @Test
    @DisplayName("Should create valid RefreshToken")
    void constructor_Success(){
        UUID id = UUID.randomUUID();
        Instant future = Instant.now().plus(7, ChronoUnit.DAYS);
        RefreshToken token = new RefreshToken(id, this.tokenValue, this.userId, future);

        assertThat(token.token()).isEqualTo(this.tokenValue);
        assertThat(token.userId()).isEqualTo(this.userId);
        assertThat(token.expiry()).isEqualTo(future);

        assertThat(token.getId()).isEqualTo(id);
        assertThat(token.getExpiry()).isEqualTo(future);
    }

    @Test
    @DisplayName("Should throw NullPointerException on empty token")
    void constructor_TokenBuildParamException(){
        assertThatThrownBy(() -> new RefreshToken(UUID.randomUUID(), "", this.userId, Instant.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Token string is required");

        assertThatThrownBy(() -> new RefreshToken(UUID.randomUUID(), null, this.userId, Instant.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Token string is required");
    }

    @Test
    @DisplayName("Should throw exception for invalid constructor arguments other than token")
    void constructor_InvalidArguments(){
        Instant future = Instant.now().plus(1, ChronoUnit.HOURS);

        assertThatThrownBy(() -> new RefreshToken(UUID.randomUUID(), this.tokenValue, null, future))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User id cannot be null");

        assertThatThrownBy(() -> new RefreshToken(UUID.randomUUID(), this.tokenValue, this.userId, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Expiry date cannot be null");
    }

    @Test
    @DisplayName("Should identify an expired token")
    void isExpired_Expired() {
        Instant past = Instant.now().minus(1, ChronoUnit.DAYS);
        RefreshToken token = new RefreshToken(UUID.randomUUID(), this.tokenValue, this.userId, past);

        assertThat(token.isExpired(Instant.now())).isTrue();
    }

    @Test
    @DisplayName("Should identify a non-expired token")
    void isExpired_NotExpired() {
        Instant future = Instant.now().plus(1, ChronoUnit.DAYS);
        RefreshToken token = new RefreshToken(UUID.randomUUID(), this.tokenValue, this.userId, future);

        assertThat(token.isExpired(Instant.now())).isFalse();
    }

    @Test
    @DisplayName("Validate should throw exception if token is expired")
    void validate_Expired() {
        Instant past = Instant.now().minus(1, ChronoUnit.DAYS);
        RefreshToken token = new RefreshToken(UUID.randomUUID(), this.tokenValue, this.userId, past);

        assertThatThrownBy(() -> token.validate(Instant.now()))
                .isInstanceOf(TokenInvalidException.class);
    }

    @Test
    @DisplayName("Validate should not throw exception if token is not expired")
    void validate_NotExpired() {
        Instant future = Instant.now().plus(1, ChronoUnit.DAYS);
        RefreshToken token = new RefreshToken(UUID.randomUUID(), this.tokenValue, this.userId, future);

        token.validate(Instant.now());
    }

    @Test
    @DisplayName("Validate should not throw exception if token is exactly equal to current time")
    void validate_ExactMatch() {
        Instant now = Instant.now();
        RefreshToken token = new RefreshToken(UUID.randomUUID(), this.tokenValue, this.userId, now);

        token.validate(now);
    }

}
