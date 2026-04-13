package org.demianov.auth.main.core.exceptions;

/**
 * The base exception for the auth-core module.
 * <p>
 *     Used for clear marking of the exceptions
 *     which are thrown in the auth-core module.
 * </p>
 * @since 0.1.0-alpha
 */
public abstract class AuthCoreException extends RuntimeException {

    /**
     * Canonical constructor.
     * @param message the error message.
     */
    public AuthCoreException(final String message) {
        super(message);
    }

    /**
     * Constructor with a custom error message and a cause.
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public AuthCoreException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a custom exception.
     * @param exception the exception.
     */
    public AuthCoreException(final Exception exception) {
        super(exception);
    }
}
