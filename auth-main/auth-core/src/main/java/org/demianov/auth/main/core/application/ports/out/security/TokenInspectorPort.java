package org.demianov.auth.main.core.application.ports.out.security;

import org.demianov.auth.main.core.domain.models.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Port to work with the access token.
 * <p>
 *     Port to abstract functionality related to
 *     the access token. THe token is not bound
 *     to any specific implementation and is not
 *     saved.
 * </p>
 * <p>
 *     The port has a port-specific exception
 *     {@link
 *     org.demianov.auth.main.core.exceptions.ports.TokenInspectorPortException
 *     }
 *     . The exception should be used by developers to handle
 *     exceptions specific to the port implementation.
 * </p>
 * @since 0.1.0-alpha
 */
public interface TokenInspectorPort {

    /**
     * Perform generation of the access token.
     * @param user user to generate the access token for.
     * @return access token.
     * @see User
     * @throws
     * org.demianov.auth.main.core.exceptions.ports.TokenInspectorPortException
     * if any error occurs during the generation process.
     */
    String generateAccessToken(User user);

    /**
     * Extract the user id from the token.
     * @param token token to extract the user id from.
     * @return user id.
     * @see User
     * @throws
     * org.demianov.auth.main.core.exceptions.ports.TokenInspectorPortException
     * if any error occurs during the extraction process.
     */
    Optional<UUID> extractUserId(String token);

    /**
     * Check if the token is valid.
     * @param token token to check.
     * @return true if the token is valid, false otherwise.
     * @throws
     * org.demianov.auth.main.core.exceptions.ports.TokenInspectorPortException
     * if any error occurs during the validation process.
     */
    boolean isValid(String token);
}
