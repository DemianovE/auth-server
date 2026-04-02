package org.demianov.auth.main.kernel.application.ports.in;

import org.demianov.auth.main.kernel.application.models.UserSummary;

/**
 * Security boundary providing identity verification
 * services for external adapters.
 * <p>
 *     This port facilitates the transition from an
 *     unauthenticated request to a recognized domain
 *     identity. It is typically consumed by security interceptors
 *     or API gateways to establish the execution context.
 * </p>
 *
 * @since 0.1.0-alpha
 */
public interface TokenShieldInputPort {

    /**
     * Authenticates the provided token and resolves the associated identity.
     * <p>
     *     The implementation must perform cryptographic
     *     signature verification and check for token expiration
     *     before attempting to identify the user and map to
     *     {@link UserSummary}.
     * </p>
     *
     * @param token the raw bearer token string to be validated.
     * @return a {@link UserSummary} instance representing
     * the authenticated user.
     */
    UserSummary verifyAndIdentify(String token);
}
