package org.demianov.auth.main.core.application.ports.in.login;

import org.demianov.auth.main.core.domain.models.User;

/**
 * The handler of the login process.
 * <p>
 *     The {@link LoginDispatcher} is responsible for
 *     running all provided handlers.
 * </p>
 * @since 0.1.0-alpha
 */
public interface LoginHandler {

    /**
     * Perform login handler action.
     * @param user - user to login.
     * @throws org.demianov.auth.main.core.exceptions.HandlerException
     * if any error occurs during the handler process.
     */
    void handle(User user);
}
