package org.demianov.auth.main.core.application.ports.in.login;

import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.CriticalSecurityException;
import org.demianov.auth.main.kernel.api.Prioritized;

/**
 * Handler to perform critical login pre-auth checks.
 * <p>
 *     The handler is responsible for performing
 *     the critical checks. The exception used will
 *     fully abort the login process.
 * </p>
 * <p>
 *     The interface uses the {@link Prioritized} interface
 *     to allow the handler to be prioritized.
 * </p>
 * @since 0.1.0-alpha
 */
@FunctionalInterface
public interface CriticalLoginHandler extends Prioritized {

    /**
     * Perform critical login handler action.
     * @param user user to login.
     * @throws CriticalSecurityException if any critical security error occurs.
     */
    void perform(User user) throws CriticalSecurityException;
}
