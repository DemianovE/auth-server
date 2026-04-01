package org.demianov.auth.main.kernel.exceptions.base;

/**
 * Throws when a requested domain entity cannot be found within the persistence layer.
 * <p>
 *     This exception triggers a 404 Not Found response in the web layer and provides a standardized
 *     {@code NOT_FOUND} error code for API consumers.
 * </p>
 * @see PlatformException
 * @since 0.1.0-alpha
 */
public class EntityNotFoundException extends PlatformException {
    public EntityNotFoundException(String message) {
        super(message, "NOT_FOUND", 404);
    }
}
