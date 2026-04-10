package org.demianov.auth.main.core.application.ports.in.login;

import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.CriticalSecurityException;

/**
 * Handler to perform critical login pre-auth checks.
 * <p>
 *     The handler is responsible for performing
 *     the critical checks. The exception used will
 *     fully abort the login process.
 * </p>
 *
 * @since 0.1.0-alpha
 */
@FunctionalInterface
public interface CriticalLoginHandler {

    /**
     * Perform critical login handler action.
     * @param user user to login.
     * @throws CriticalSecurityException if any critical security error occurs.
     */
    void perform(User user) throws CriticalSecurityException;
}
