package org.demianov.auth.main.core.exceptions.ports;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Exception thrown when the token inspector port operation failed.
 * Should be used by the developers implementing the token inspector port.
 */
public class TokenInspectorPortException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Constructor with a custom error message.
     * @param message error message.
     * @param cause the cause of the exception.
     */
    public TokenInspectorPortException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
