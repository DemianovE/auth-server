package org.demianov.auth.main.sdk.exceptions;

import org.demianov.auth.main.kernel.exceptions.base.ActionForbiddenException;

/**
 * Exception thrown when the user account is not verified.
 * <p>
 *     This exception is a child of {@link ActionForbiddenException}
 *     and triggers 403 system response in web layer.
 *     The exception message is passed to the client.
 * <p/>
 * @see ActionForbiddenException
 * @since 0.1.0-alpha
 */
public class AccountRequiresVerificationException
        extends ActionForbiddenException
        implements AuthApiException {

    /**
     * Canonical constructor for the account requires verification exception.
     */
    public AccountRequiresVerificationException() {
        super("User is not verified.");
    }
}
