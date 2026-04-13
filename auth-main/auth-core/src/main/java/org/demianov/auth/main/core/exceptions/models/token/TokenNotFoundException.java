package org.demianov.auth.main.core.exceptions.models.token;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * Exception thrown when a token is not found.
 */
public final class TokenNotFoundException extends AuthCoreException
        implements LoginPortExceptions {

    /**
     * Canonical constructor.
     * @implNote The error message is "Token not missing or non-existing"
     */
    public TokenNotFoundException() {
        super("Token not missing or non existing.");
    }
}
