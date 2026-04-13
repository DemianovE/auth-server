package org.demianov.auth.main.core.exceptions.tags;

import org.demianov.auth.main.core.exceptions.CriticalSecurityException;
import org.demianov.auth.main.core.exceptions.DataAccessException;
import org.demianov.auth.main.core.exceptions.HandlerException;
import org.demianov.auth.main.core.exceptions.models.token.TokenException;
import org.demianov.auth.main.core.exceptions.models.token.TokenInvalidException;
import org.demianov.auth.main.core.exceptions.models.token.TokenNotFoundException;
import org.demianov.auth.main.core.exceptions.models.user.PasswordMismatchException;
import org.demianov.auth.main.core.exceptions.models.user.UserAccountException;
import org.demianov.auth.main.core.exceptions.models.user.UserNotFoundException;
import org.demianov.auth.main.core.exceptions.ports.UnexpectedSystemException;
import org.demianov.auth.main.core.exceptions.ports.PasswordHasherPortException;
import org.demianov.auth.main.core.exceptions.ports.SecureStringGeneratorPortException;
import org.demianov.auth.main.core.exceptions.ports.TokenInspectorPortException;

/**
 * This tag is used to clearly define which type of
 * exceptions can be wraped in the
 * {@link org.demianov.auth.main.core.application.models.LoginResult.Failure}.
 */
public sealed interface LoginPortExceptions permits
        TokenException, TokenInvalidException, TokenNotFoundException,
        PasswordMismatchException, UserAccountException, UserNotFoundException,
        PasswordHasherPortException, SecureStringGeneratorPortException,
        TokenInspectorPortException, UnexpectedSystemException,

        CriticalSecurityException, DataAccessException, HandlerException {
}
