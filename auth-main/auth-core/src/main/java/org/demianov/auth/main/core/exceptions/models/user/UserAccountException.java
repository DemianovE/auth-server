package org.demianov.auth.main.core.exceptions.models.user;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Exception thrown when the user account
 * is not suited for the action.
 */
public final class UserAccountException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Canonical constructor.
     * @param email - user email.
     * @implNote The error message is "The account of the user with
     * email {email} is locked."
     */
    public UserAccountException(final String email) {
        super("The account of user with email " + email + " is locked.");
    }
}
