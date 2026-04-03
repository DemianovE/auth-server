package org.demianov.auth.main.sdk.exceptions;

import org.demianov.auth.main.kernel.exceptions.base.AuthenticationFailureException;

/**
 * Throws exceptions when the provided credentials are invalid.
 * <p>
 *     This exception is child of {@link AuthenticationFailureException}
 *     and throws 401 system response in web layer.
 * </p>
 * @see AuthenticationFailureException
 * @since 0.1.0-alpha
 */
public class InvalidCredentialsException
        extends AuthenticationFailureException
        implements AuthApiException {

    /**
     * Canonical constructor for the invalid credentials' exception.
     * @implNote The error message is "Provided email or password are incorrect"
     */
    public InvalidCredentialsException() {
        super("Provided email or password are incorrect");
    }
}
