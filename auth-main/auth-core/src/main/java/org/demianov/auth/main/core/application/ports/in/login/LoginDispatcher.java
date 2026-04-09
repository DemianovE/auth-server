package org.demianov.auth.main.core.application.ports.in.login;

import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.domain.models.User;

/**
 * The dispatcher of the login process.
 * <p>
 *     Is used for the ende-cases configuration.
 * </p>
 *
 * @since 0.1.0-alpha
 */
public interface LoginDispatcher {

    /**
     * Perform login dispatch action.
     * <p>
     *     The dispatcher should handle the handler possible
     *     exceptions which are in type
     *     {@link org.demianov.auth.main.core.exceptions.HandlerException}.
     *     This exception should be wraped in the
     *     {@link LoginResult.Failure}.
     * </p>
     * @param user user to login.
     * @return generated token pairs' DTO response.
     */
    LoginResult dispatch(User user);
}
