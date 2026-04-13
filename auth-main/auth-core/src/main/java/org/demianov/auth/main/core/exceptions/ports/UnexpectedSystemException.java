package org.demianov.auth.main.core.exceptions.ports;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Exception thrown when the system throws an unexpected exception.
 * @since 0.1.0-alpha
 */
public final class UnexpectedSystemException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Constructor with a custom error message.
     * @param exception the exception.
     */
    public UnexpectedSystemException(
            final Exception exception) {
        super(exception);
    }
}
