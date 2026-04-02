package org.demianov.auth.main.kernel.exceptions.base;

/**
 * Throws when a requested domain entity cannot be found
 * within the persistence layer.
 * <p>
 *     This exception triggers a 404 Not Found response in
 *     the web layer and provides a standardized
 *     {@code NOT_FOUND} error code for API consumers.
 * </p>
 * @see PlatformException
 * @since 0.1.0-alpha
 */
public class EntityNotFoundException extends PlatformException {

    /**
     * Canonical Constructor for the not found entity exception.
     * @param message - error message.
     */
    public EntityNotFoundException(final String message) {
        super(
                message,
                ExceptionConstants.ErrorTag.NOT_FOUND,
                ExceptionConstants.ErrorCodes.NOT_FOUND
        );
    }
}
