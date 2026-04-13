package org.demianov.auth.main.core.exceptions;

import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Throws exception when an error occurs in the handler.
 * @since 0.1.0-alpha
 */
public final class HandlerException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Canonical constructor.
     * @param message error message.
     * @param cause the cause of the exception.
     */
    public HandlerException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
