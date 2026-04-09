package org.demianov.auth.main.core.application.services.use_case.login;

import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.ports.out.security.TokenGeneratorPort;
import org.demianov.auth.main.core.application.ports.in.login.LoginDispatcher;
import org.demianov.auth.main.core.application.ports.in.login.LoginHandler;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.HandlerException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class DefaultLoginDispatcher implements LoginDispatcher {
    /** The port to work with the token generation. */
    private final TokenGeneratorPort tokenGenerator;
    /** The list of handlers to work with the login process. */
    private final List<LoginHandler> handlers;

    /**
     * Canonical constructor. Perform mandatory tokenGenerator validation.
     * @param tokenGeneratorParam the port to work with the token generation
     * @param handlersParam the list of handlers to work with the login process
     */
    public DefaultLoginDispatcher(
            final TokenGeneratorPort tokenGeneratorParam,
            final List<LoginHandler> handlersParam) {
        this.tokenGenerator = Objects.requireNonNull(tokenGeneratorParam,
                "tokenGenerator");
        this.handlers = (handlersParam != null)
                ? List.copyOf(handlersParam)
                : Collections.emptyList();
    }

    @Override
    public LoginResult dispatch(final User user) {
        try {
            this.handlers.forEach(handler ->
                    handler.handle(user));
        } catch (HandlerException e) {
            return new LoginResult.Failure(e);
        }

        return this.tokenGenerator.generate(user);
    }
}
