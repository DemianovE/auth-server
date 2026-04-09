package org.demianov.auth.main.core.exceptions.models.user;

import org.demianov.auth.main.core.exceptions.AuthCoreException;

/**
 * Exception thrown when the user email is already taken.
 */
public class UserEmailAlreadyTakenException extends AuthCoreException {

    /**
     * Canonical constructor.
     * @param email - user email.
     * @implNote The error message is "The account of the user with
     * email {email} is already taken."
     */
    public UserEmailAlreadyTakenException(final String email) {
        super("The account of user with email " + email + " is already taken.");
    }
}
