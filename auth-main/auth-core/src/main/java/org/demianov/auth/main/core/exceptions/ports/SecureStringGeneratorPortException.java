package org.demianov.auth.main.core.exceptions.ports;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Exception thrown when the secure string generator port
 * operation failed. Should be used by the developers
 * implementing the secure string generator port.
 */
public final class SecureStringGeneratorPortException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Constructor with a custom error message.
     * @param message error message.
     * @param cause the cause of the exception.
     */
    public SecureStringGeneratorPortException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
