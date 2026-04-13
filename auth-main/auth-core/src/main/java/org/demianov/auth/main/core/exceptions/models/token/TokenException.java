package org.demianov.auth.main.core.exceptions.models.token;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Exception thrown when the token is invalid in any way.
 * @since 0.1.0-alpha
 */
public final class TokenException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Canonical constructor.
     * @param message - error message.
     */
    public TokenException(final String message) {
        super(message);
    }
}
