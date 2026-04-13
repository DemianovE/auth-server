package org.demianov.auth.main.core.application.ports.out.listeners;

import org.demianov.auth.main.core.application.models.LoginCommand;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.kernel.api.Prioritized;

/**
 * Listener to perform login handler action.
 * <p>
 *     Can be used to perform the <i>after-login</i>
 *     actions.
 * </p>
 * <p>
 *     The interface uses the {@link Prioritized} interface
 *     to allow the handler to be prioritized.
 * </p>
 * @since 0.1.0-alpha
 */
@FunctionalInterface
public interface LoginListener extends Prioritized {

    /**
     * Perform login listen action on the successful login.
     * @param user user that was logged in.
     */
    void onSuccess(User user);

    /**
     * Perform login listen action on the failed login.
     *
     * @param request the login command request payload.
     */
    default void onFailure(LoginCommand request) {

    }
}
