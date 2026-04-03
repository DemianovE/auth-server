package org.demianov.auth.main.sdk.exceptions;

import org.demianov.auth.main.kernel.exceptions.base.AuthenticationFailureException;

/**
 * Throws exceptions when the token is invalid or expired.
 * <p>
 *     This exception is child of {@link AuthenticationFailureException}
 *     and triggers 401 system response in web layer and provides.
 * </p>
 * @see AuthenticationFailureException
 * @since 0.1.0-alpha
 */
public class TokenInvalidException
        extends AuthenticationFailureException
        implements AuthApiException {

    /**
     * Canonical constructor for the token invalid exception.
     * @implNote The error message is "Token is invalid or expired."
     */
    public TokenInvalidException() {
        super("Token is invalid or expired.");
    }
}
