package org.demianov.auth.main.core.application.services.domain_services;

import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.ports.out.persistence.RefreshTokenRepoPort;
import org.demianov.auth.main.core.application.ports.out.security.SecureStringGeneratorPort;
import org.demianov.auth.main.core.application.ports.out.security.TokenGeneratorPort;
import org.demianov.auth.main.core.application.ports.out.security.TokenInspectorPort;
import org.demianov.auth.main.core.application.ports.out.persistence.UserRepoPort;

import org.demianov.auth.main.core.domain.models.RefreshToken;
import org.demianov.auth.main.core.domain.models.User;

import org.demianov.auth.main.core.exceptions.ports.TokenInspectorPortException;
import org.demianov.auth.main.core.exceptions.models.token.TokenNotFoundException;
import org.demianov.auth.main.core.exceptions.models.user.UserNotFoundException;
import org.demianov.auth.main.core.exceptions.ports.SecureStringGeneratorPortException;
import org.demianov.auth.main.core.exceptions.DataAccessException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class TokenGeneratorService implements TokenGeneratorPort {
    /** The port to work with the secure string generator. */
    private final SecureStringGeneratorPort secureStringGenerator;
    /** The port to work with the token inspector. */
    private final TokenInspectorPort tokenInspector;
    /** The port to work with the refresh token repository. */
    private final RefreshTokenRepoPort refreshTokenRepo;
    /** The port to work with the user repository. */
    private final UserRepoPort userRepo;
    /** The clock, used in the whole of a system. */
    private final Clock clock;

    /** The duration of the refresh token. */
    private final Duration refreshTokenTtl;
    /** The duration of the access token. */
    private final Duration accessTokenTtl;

    /**
     * Canonical constructor. Perform mandatory field validation.
     * @param secureStringGeneratorParam secure string generator.
     * @param tokenInspectorParam token inspector.
     * @param refreshTokenRepoParam refresh token repository.
     * @param userRepoParam user repository.
     * @param clockParam clock, used in the whole of a system.
     * @param refreshTokenTtlParam the duration of the refresh token.
     * @param accessTokenTtlParam the duration of the access token.
     *
     * @throws NullPointerException if any of the fields is null.
     */
    public TokenGeneratorService(
            final SecureStringGeneratorPort secureStringGeneratorParam,
            final TokenInspectorPort tokenInspectorParam,
            final RefreshTokenRepoPort refreshTokenRepoParam,
            final UserRepoPort userRepoParam,
            final Clock clockParam,
            final Duration refreshTokenTtlParam,
            final Duration accessTokenTtlParam) {
        this.secureStringGenerator = Objects.requireNonNull(
                secureStringGeneratorParam,
                "secureStringGenerator cannot be null");
        this.tokenInspector = Objects.requireNonNull(tokenInspectorParam,
                "tokenInspector cannot be null");
        this.refreshTokenRepo = Objects.requireNonNull(refreshTokenRepoParam,
                "refreshTokenRepo cannot be null");
        this.userRepo = Objects.requireNonNull(userRepoParam,
                "userRepo cannot be null");
        this.clock = Objects.requireNonNull(clockParam,
                "clock cannot be null");
        this.refreshTokenTtl = Objects.requireNonNull(refreshTokenTtlParam,
                "refreshTokenTtl cannot be null");
        this.accessTokenTtl = Objects.requireNonNull(accessTokenTtlParam,
                "accessTokenTtl cannot be null");
    }

    @Override
    public LoginResult generate(final User user) {
        try {
            Instant now = this.clock.instant();

            String accessToken = this.tokenInspector.generateAccessToken(user);

            RefreshToken refreshToken = RefreshToken.create(
                    UUID.randomUUID(),
                    this.secureStringGenerator.generate(),
                    user.getId(),
                    now.plus(this.refreshTokenTtl)
            );

            this.refreshTokenRepo.save(refreshToken);

            return new LoginResult.Success(
                    accessToken,
                    refreshToken.getToken(),
                    this.accessTokenTtl,
                    this.refreshTokenTtl);

        } catch (DataAccessException
                 | TokenInspectorPortException
                 | SecureStringGeneratorPortException e) {
            return new LoginResult.Failure(e);
        }
    }

    @Override
    public LoginResult refresh(final String tokenId) {
        try {
            RefreshToken refreshToken = this.refreshTokenRepo.findByToken(
                    tokenId)
                    .orElseThrow(TokenNotFoundException::new);

            refreshToken.validate(Instant.now(this.clock));

            User user = this.userRepo.findById(refreshToken.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(
                            refreshToken.getUserId()));

            LoginResult result = generate(user);
            this.refreshTokenRepo.delete(refreshToken);
            return result;
        } catch (DataAccessException
                 | TokenNotFoundException
                 | UserNotFoundException e) {
            return new LoginResult.Failure(e);
        }
    }
}
