package org.demianov.auth.main.core.exceptions;

import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Thrown when a critical security error occurs.
 * @since 0.1.0-alpha
 */
public final class CriticalSecurityException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Constructor.
     * @param message error message.
     */
    public CriticalSecurityException(final String message) {
        super(message);
    }
}
