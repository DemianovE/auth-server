package org.demianov.auth.main.core.domain.models;

import org.demianov.auth.main.core.exceptions.models.token.TokenInvalidException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representation of the refresh token.
 * <p>
 *     The model holds the data required to work with
 *     the refresh token. Each token is associated with a
 *     {@link User}
 * </p>
 * <p>
 *       The model has classic getters for convenience, due to
 *       being part of the domain models.
 * </p>
 *
 * @param id refresh token id
 * @param token refresh token
 * @param userId user id
 * @param expiry token expiry date
 *
 * @since 0.1.0-alpha
 */
public record RefreshToken(
        UUID id,
        String token,
        UUID userId,
        Instant expiry) {

    /**
     * Canonical constructor. Perform mandatory field validation.
     * @param id - refresh token id/token itself^
     * @param userId - user id
     * @param expiry - token expiry date
     * @throws NullPointerException if any of the fields is null.
     */
    public RefreshToken {
        Objects.requireNonNull(id,
                "Refresh token id cannot be null");
        Objects.requireNonNull(userId,
                "User id cannot be null");
        Objects.requireNonNull(expiry,
                "Expiry date cannot be null");

        if (token == null || token.isBlank()) {
            throw new NullPointerException("Token string is required");
        }
    }

    /**
     * Factory method. For specific object creation.
     * @param id - refresh token id
     * @param token - refresh token
     * @param userId - user id
     * @param expiry - token expiry date
     * @return - refresh token.
     */
    public static RefreshToken create(
            final UUID id,
            final String token,
            final UUID userId,
            final Instant expiry) {
        return new RefreshToken(id, token, userId, expiry);
    }

    /**
     * Check if the refresh token is expired.
     * @param now - current time.
     * @return - true if the refresh token is expired, false otherwise.
     */
    public boolean isExpired(final Instant now) {
        return now.isAfter(this.expiry);
    }

    /**
     * Validate the refresh token.
     * @param now - current time.
     */
    public void validate(final Instant now) {
        if (isExpired(now)) {
            throw new TokenInvalidException();
        }
    }

    // -- Getters (Java standard for convenience) --

    /**
     * Returns refresh token id.
     * @return - refresh token id.
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * Returns refresh token.
     * @return - token itself.
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Returns user id.
     * @return - user id.
     */
    public UUID getUserId() {
        return this.userId;
    }

    /**
     * Returns token expiry date.
     * @return - token expiry date.
     */
    public Instant getExpiry() {
        return this.expiry;
    }
}
