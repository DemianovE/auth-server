package org.demianov.auth.main.kernel.exceptions.base;

/**
 * Throws when an unexpected server side error occurs.
 * <p>
 *     This exception triggers a 500 Internal Server Error response in the web layer and provides a standardized
 *     {@code INTERNAL_SERVER_ERROR} error code for API consumers.
 * </p>
 * @see PlatformException
 * @since 0.1.0-alpha
 */
public class SystemErrorException extends PlatformException {
    public SystemErrorException() {
        super("An unexpected internal error occurred", "SYSTEM_ERROR", 500);
    }
}
