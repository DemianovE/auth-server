package org.demianov.auth.main.kernel.exceptions.base;

/**
 * Throws exception when the API consumer provides a wrong set of parameters.
 * <p>
 *     This exception triggers a 400 Bad Request response in the web
 *     layer and provides a standardized {@code WRONG_PARAMETERS} error
 *     code for API consumers.
 * </p>
 * @see PlatformException
 * @since 0.1.0-alpha
 */
public class WrongParametersException extends PlatformException {

    /**
     * Canonical Constructor for the wrong parameters' exception.
     * @param message - error message.
     */
    public WrongParametersException(final String message) {
        super(
                message,
                ExceptionConstants.ErrorTag.BAD_REQUEST,
                ExceptionConstants.ErrorCodes.BAD_REQUEST
        );
    }
}
