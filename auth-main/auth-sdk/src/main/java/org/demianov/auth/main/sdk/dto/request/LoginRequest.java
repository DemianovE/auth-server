package org.demianov.auth.main.sdk.dto.request;

import java.util.Objects;

/**
 * DTO used for authentication request parameters.
 * <p>
 *     This DTO is used to pass parameters for the
 *     login process in the system.
 * </p>
 * @param email user email
 * @param password user password
 *
 * @since 0.1.0-alpha
 */
public record LoginRequest(
        String email,
        String password) {

    /**
     * Canonical constructor. Perform mandatory field validation.
     * @param email - user email
     * @param password - user password
     *
     * @throws NullPointerException if any of the fields is null.
     */
    public LoginRequest {
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(password, "password cannot be null");
    }
}
