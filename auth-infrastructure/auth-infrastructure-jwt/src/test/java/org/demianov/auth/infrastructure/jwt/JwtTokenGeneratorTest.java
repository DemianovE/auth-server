package org.demianov.auth.infrastructure.jwt;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.models.token.TokenInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenGeneratorTest {

    private JwtTokenGenerator generator;
    private final String base64Secret = Base64.getEncoder().encodeToString(
            "super-secret-key-that-must-be-at-least-256-bits-long-for-hmac".getBytes()
    );

    private final Duration tokenTtl = Duration.ofMinutes(15);
    private final Instant fixedNow = Instant.parse("2026-03-01T10:00:00Z");
    private final Clock fixedClock = Clock.fixed(fixedNow, ZoneId.of("UTC"));

    @BeforeEach
    void setUp(){
        generator = new JwtTokenGenerator(base64Secret, tokenTtl, fixedClock);
    }

    @Test
    @DisplayName("Should generate a valid JWT and extract the correct UserId")
    void generate_Success() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        String token = generator.generateAccessToken(user);
        Optional<UUID> actual = generator.extractUserId(token);

        assertThat(token).isNotBlank();
        assertThat(actual).isPresent().contains(userId);
        assertThat(generator.isValid(token)).isTrue();
    }

    @Test
    @DisplayName("Should throw TokenInvalidException for late token")
    void extractUserId_TokenInvalidException_ExpiredToken() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        String token = generator.generateAccessToken(user);

        Clock futureClock = Clock.fixed(fixedNow.plus(Duration.ofMinutes(20)), ZoneId.of("UTC"));
        JwtTokenGenerator futureGenerator = new JwtTokenGenerator(base64Secret, tokenTtl, futureClock);

        assertThatThrownBy(() -> futureGenerator.extractUserId(token))
                .isInstanceOf(TokenInvalidException.class)
                .hasMessage("Token has expired.");
    }

    @Test
    @DisplayName("Should throw TokenInvalidException when signature is tampered")
    void extractUserId_TokenInvalidException_SignatureTampered() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        String token = generator.generateAccessToken(user);

        String tamperedToken = token.substring(0, token.length() - 1);

        assertThatThrownBy(() -> generator.extractUserId(tamperedToken))
                .isInstanceOf(TokenInvalidException.class)
                .hasMessage("Token signature is invalid.");
    }

    @Test
    @DisplayName("Should throw TokenInvalidException when token format is invalid")
    void extractUserId_TokenInvalidException_InvalidFormat() {
        String invalidToken = "invalid-token";

        assertThatThrownBy(() -> generator.extractUserId(invalidToken))
                .isInstanceOf(TokenInvalidException.class)
                .hasMessage("Token format is invalid.");
    }

    @Test
    @DisplayName("Should return empty list for unexpected internal error")
    void extractUserId_TokenInvalidException_UnexpectedError() {
        Optional<UUID> actual = generator.extractUserId("");

        assertThat(actual).isEmpty();
    }
}
