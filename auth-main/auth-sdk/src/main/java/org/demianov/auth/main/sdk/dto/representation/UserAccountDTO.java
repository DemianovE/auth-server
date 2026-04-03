package org.demianov.auth.main.sdk.dto.representation;

import org.demianov.auth.main.kernel.domain.models.UserStatus;

import java.util.Objects;
import java.util.UUID;

/**
 * DTO representation of the user domain model.
 * <p>
 *     Used to pass user data to the client.
 *     The DTO is a boilerplate but good to have for versioning and
 *     in case of future changes.
 * </p>
 * @param userId  user id
 * @param email  user email
 * @param status  user status
 * @see UserStatus
 *
 * @since 0.1.0-alpha
 */
public record UserAccountDTO(
        UUID userId,
        String email,
        UserStatus status) {

    /**
     * Canonical constructor. Perform mandatory field validation.
     * @param userId userId of the user
     * @param email email of the user
     * @param status status of the user
     *
     * @throws NullPointerException if any of the fields is null.
     */
    public UserAccountDTO {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(status, "status cannot be null");
    }
}
