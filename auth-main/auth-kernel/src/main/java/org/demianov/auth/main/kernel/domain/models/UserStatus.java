package org.demianov.auth.main.kernel.domain.models;

/**
 * Represents the lifecycle state of a
 * {@code User}.
 *
 * <p>
 *     The status determines the operational capabilities of
 *     an account and dictates how the authentication engine
 *     handles login attempts and token issuance.
 * </p>
 * <p>The system enforces the following behavior based on status:</p>
 * <ul>
 *     <li><b>PENDING:</b> Restricted state. Authentication attempts
 *          will trigger an
 *          {@code AccountRequiresVerificationException}.</li>
 *     <li><b>ACTIVE:</b> Full operational state. All authorized
 *         actions are permitted based on the assigned {@link UserRole}.</li>
 *     <li><b>BLOCKED:</b> Administrative lock. All actions
 *         are strictly prohibited, though the identity record is
 *         retained to prevent duplicate registrations.</li>
 * </ul>
 * @since 0.1.0-alpha
 */
public enum UserStatus {
    /**
     * Initial state for newly created users.
     * Requires external confirmation (e.g., email verification)
     * to transition to {@link #ACTIVE}.
     */
    PENDING,

    /**
     * The primary operational state.
     * Indicates the user has been verified and is permitted to
     * interact with protected resources.
     */
    ACTIVE,

    /**
     * Security-terminated state.
     * Indicates the account has been disabled due to policy
     * violations or administrative action.
     */
    BLOCKED
}
