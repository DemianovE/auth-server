package org.demianov.auth.main.core.application.services.use_case.login;

import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.ports.in.login.LoginDispatcher;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.kernel.application.annotations.Internal;

/**
 * This implementation does nothing.
 * {@inheritDoc}
 */
@Internal
public final class DefaultLoginDispatcher implements LoginDispatcher {

    /**
     * Canonical constructor.
     */
    public DefaultLoginDispatcher() {
    }

    @Override
    public LoginResult dispatch(
            final LoginResult loginResult,
            final User user) {
        return loginResult;
    }
}
