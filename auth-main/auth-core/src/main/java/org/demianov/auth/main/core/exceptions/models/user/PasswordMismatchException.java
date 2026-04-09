package org.demianov.auth.main.core.exceptions.models.user;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Throws exception when the password is incorrect.
 */
public class PasswordMismatchException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Canonical constructor.
     * @implNote The error message is "Password is incorrect."
     */
    public PasswordMismatchException() {
        super("Password is incorrect.");
    }
}
