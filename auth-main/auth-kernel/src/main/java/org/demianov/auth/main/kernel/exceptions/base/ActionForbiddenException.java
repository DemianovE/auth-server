package org.demianov.auth.main.kernel.exceptions.base;

/**
 * Throws when clients' action is forbidden.
 * <p>
 *     This exception triggers a 403 Forbidden response in
 *     the web layer and provides a standardized {@code FORBIDDEN}
 *     error code for API consumers.
 * </p>
 * @see PlatformException
 * @since 0.1.0-alpha
 */
public class ActionForbiddenException extends PlatformException {

    /**
     * Canonical constructor for the forbidden action exception.
     * 403 Forbidden response.
     * @param message error message.
     */
    public ActionForbiddenException(final String message) {
        super(
                message,
                ExceptionConstants.ErrorTag.FORBIDDEN,
                ExceptionConstants.ErrorCodes.FORBIDDEN
        );
    }
}
