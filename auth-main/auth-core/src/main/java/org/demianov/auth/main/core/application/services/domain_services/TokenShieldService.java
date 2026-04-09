package org.demianov.auth.main.core.application.services.domain_services;

import org.demianov.auth.main.kernel.application.models.UserSummary;
import org.demianov.auth.main.kernel.application.ports.in.TokenShieldInputPort;

import org.demianov.auth.main.core.application.ports.out.security.TokenInspectorPort;
import org.demianov.auth.main.core.application.ports.out.persistence.UserRepoPort;

import org.demianov.auth.main.core.domain.models.User;

import org.demianov.auth.main.core.exceptions.models.token.TokenInvalidException;

import java.util.Objects;

public final class TokenShieldService implements TokenShieldInputPort {
    /** The port to work with the JWT token. */
    private final TokenInspectorPort jwtInspector;
    /** the port to work with the user repository. */
    private final UserRepoPort userRepo;

    /**
     * Canonical constructor. Perform mandatory field validation.
     * @param jwtInspectorParam the port to work with the JWT token
     * @param userRepoParam the port to work with the user repository
     * @throws NullPointerException if any of the fields is null.
     */
    public TokenShieldService(
            final TokenInspectorPort jwtInspectorParam,
            final UserRepoPort userRepoParam) {
        this.jwtInspector = Objects.requireNonNull(jwtInspectorParam,
                "jwtInspector");
        this.userRepo = Objects.requireNonNull(userRepoParam,
                "userRepo");
    }

    @Override
    public UserSummary verifyAndIdentify(final String token) {
        return this.jwtInspector.extractUserId(token)
                .flatMap(this.userRepo::findById)
                .filter(User::isActive)
                .map(User::toSummary)
                .orElseThrow(TokenInvalidException::new);
    }
}
