package org.demianov.auth.main.core.application.ports.in.login;

import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.domain.models.User;

/**
 * The dispatcher of the login process.
 * <p>
 *     This dispatcher can be used
 *     to modify the output of the login process.
 *     As example: MFA.
 * </p>
 *
 * <p>
 *     The default implementation changes nothing.
 * </p>
 *
 * @since 0.1.0-alpha
 */
public interface LoginDispatcher {

    /**
     * Perform login dispatch action.
     * @param user user to login.
     * @param result login result.
     * @return modified result.
     */
    LoginResult dispatch(LoginResult result, User user);
}
