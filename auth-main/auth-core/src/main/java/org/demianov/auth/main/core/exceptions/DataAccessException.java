package org.demianov.auth.main.core.exceptions;

import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Throws when the data access operation failed.
 * @since 0.1.0-alpha
 */
public final class DataAccessException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Canonical constructor.
     * @param message error message.
     * @param cause the cause of the exception.
     */
    public DataAccessException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
