package org.demianov.auth.main.kernel.application.models;

import org.demianov.auth.main.kernel.domain.models.Email;
import org.demianov.auth.main.kernel.domain.models.UserStatus;

import java.util.Objects;
import java.util.UUID;

/**
 * A lightweight projection of the {@link org.demianov.auth.main.core.domain.models.User} entity for application-layer use cases.
 * <p>
 *     This record encapsulates identity and lifecycle state, designed for efficient
 *     serialization and transfer within the internal application services.
 * </p>
 * @param id The unique identifier of the user.
 * @param email The validated {@link Email}.
 * @param status The current {@link UserStatus} defining the account lifecycle.
 *
 * @since 0.1.0-alpha
 */
public record UserSummary(
        UUID id,
        Email email,
        UserStatus status
) {

    /**
     * Canonical constructor with mandatory field validation.
     * @throws NullPointerException if {@code id}, {@code email}, or {@code status} is null.
     */
    public UserSummary {
        Objects.requireNonNull(id, "User id is required");
        Objects.requireNonNull(email, "User email is required");
        Objects.requireNonNull(status, "User status is required");
    }
}
