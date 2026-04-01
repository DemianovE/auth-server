package org.demianov.auth.main.kernel.domain.models;

/**
 * Defines the access levels and authorities available to User within the security domain.
 *
 * <p>
 *     Roles are used by the {@link org.demianov.auth.main.core.domain.models.User}
 *     entity to determine authorization boundaries and execute role-specific business logic.
 * </p>
 * <p>
 *     During the authentication lifecycle, these roles are encoded into the
 *     {@link org.demianov.auth.main.sdk.dto.response.AuthTokensDTO}, allowing downstream
 *     services to perform stateless authorization checks.
 * </p>
 * @since 0.1.0-alpha
 */
public enum UserRole {
    /**
     * Standard authority level for regular customers of the application
     */
    USER,

    /**
     * Elevated authority level with access to administrative functions and systems.
     */
    ADMIN
}
