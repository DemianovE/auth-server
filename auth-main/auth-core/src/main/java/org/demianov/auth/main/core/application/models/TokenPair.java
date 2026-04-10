package org.demianov.auth.main.core.application.models;

import java.time.Duration;
import java.util.Objects;

/**
 * DTO used to return the access and refresh tokens.
 * @param accessToken access token String.
 * @param refreshToken refresh token String.
 * @param expiresIn access token expiration time.
 * @param refreshTokenTtl refresh token expiration time.
 */
public record TokenPair(
        String accessToken,
        String refreshToken,

        Duration expiresIn,
        Duration refreshTokenTtl
) {

    /**
     * Canonical constructor. Perform mandatory field validation.
     * @param accessToken access token.
     * @param refreshToken refresh token.
     * @param expiresIn access token expiration time.
     * @param refreshTokenTtl refresh token expiration time.
     */
    public TokenPair {
        Objects.requireNonNull(accessToken,
                "accessToken cannot be null");
        Objects.requireNonNull(refreshToken,
                "refreshToken cannot be null");
        Objects.requireNonNull(expiresIn,
                "expiresIn cannot be null");
        Objects.requireNonNull(refreshTokenTtl,
                "refreshTokenTtl cannot be null");
    }
}
