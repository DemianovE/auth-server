package org.demianov.auth.main.sdk.dto.response;

import java.time.Duration;
import java.util.Objects;

/**
 * DTO used for authentication request response.
 * <p>
 *     This DTO is used to return the access and refresh tokens
 *     as well as expiration times.
 * </p>
 * @param token access token
 * @param refresh refresh token
 * @param expiresIn access token expiration time
 * @param refreshTokenTtl refresh token expiration time
 *
 * @since 0.1.0-alpha
 */
public record AuthTokensDTO(
        String token,
        String refresh,

        Duration expiresIn,
        Duration refreshTokenTtl
) {

    /**
     * Canonical constructor. Perform mandatory field validation.
     * <p>
     *     Throws {@link NullPointerException} if any of the fields is null.
     * </p>
     * @param token access token.
     * @param refresh refresh token.
     * @param expiresIn access token expiration time.
     * @param refreshTokenTtl refresh token expiration time.
     * @throws NullPointerException if any of the fields is null.
     */
    public AuthTokensDTO {
        Objects.requireNonNull(token,
                "token cannot be null");
        Objects.requireNonNull(refresh,
                "refresh cannot be null");
        Objects.requireNonNull(expiresIn,
                "expiresIn cannot be null");
        Objects.requireNonNull(refreshTokenTtl,
                "refreshTokenTtl cannot be null");
    }
}
