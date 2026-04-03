package org.demianov.auth.main.kernel.exceptions.base;

/**
 * Thrown when the server authorization process fails
 * <p>
 *     This exception triggers 401 system response
 *     in web layer and provides standartalized
 *     {@code UNAUTHORIZED} error code for API consumers.
 * </p>
 * @see PlatformException
 * @since 0.1.0-alpha
 */
public class AuthenticationFailureException extends PlatformException {

    /**
     * Canonical constructor for unauthorized exception.
     * @param message the error message.
     */
    public AuthenticationFailureException(final String message) {
        super(
                message,
                ExceptionConstants.ErrorTag.UNAUTHORIZED,
                ExceptionConstants.ErrorCodes.UNAUTHORIZED
        );
    }
}
