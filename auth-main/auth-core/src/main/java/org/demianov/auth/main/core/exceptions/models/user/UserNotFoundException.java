package org.demianov.auth.main.core.exceptions.models.user;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

import java.util.UUID;

/**
 * Exception thrown when a user is not found in the domain.
 */
public class UserNotFoundException extends AuthCoreException
        implements LoginPortExceptions {
    /**
     * Canonical constructor.
     * @param email - user email.
     * @implNote The error message is "User with email {email} was not
     * found in the domain."
     */
    public UserNotFoundException(final String email) {
        super("User with email " + email + " was not found in the domain.");
    }

    /**
     * Constructor with a user id.
     * @param id - user id.
     * @implNote The error message is "User with id {id} was not found
     * in the domain."
     */
    public UserNotFoundException(final UUID id) {
        super("User with id " + id + " was not found in the domain.");
    }
}
