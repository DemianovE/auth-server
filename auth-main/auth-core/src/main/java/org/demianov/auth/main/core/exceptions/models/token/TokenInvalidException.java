package org.demianov.auth.main.core.exceptions.models.token;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Exception thrown when the token is invalid or expired.
 */
public class TokenInvalidException  extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Canonical constructor. Default error message.
     * @implNote The error message is "Token is invalid or expired."
     */
    public TokenInvalidException() {
        super("Token is invalid or expired.");
    }

    /**
     * Constructor with a custom error message.
     * @param message error message.
     */
    public TokenInvalidException(final String message) {
        super(message);
    }
}
