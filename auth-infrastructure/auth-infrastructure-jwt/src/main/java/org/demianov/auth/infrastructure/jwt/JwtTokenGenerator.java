package org.demianov.auth.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.demianov.auth.main.core.application.ports.out.common.AbstractGuard;
import org.demianov.auth.main.core.application.ports.out.security.TokenInspectorPort;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.models.token.TokenInvalidException;
import org.demianov.auth.main.core.exceptions.ports.TokenInspectorPortException;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the TokenInspectorPort.
 */
public class JwtTokenGenerator
        extends AbstractGuard<TokenInspectorPortException>
        implements TokenInspectorPort {

    /** The secret key used to sign the JWT token. */
    private final SecretKey key;
    /** The clock used to generate the token. */
    private final Clock clock;
    /** The token time to live. */
    private final Duration tokenTtl;

    /**
     * Constructor.
     * @param base64Secret base64 encoded secret key.
     * @param tokenTtlParams token time to live.
     * @param clockParam clock used to generate the token.
     */
    public JwtTokenGenerator(
            final String base64Secret,
            final Duration tokenTtlParams,
            final Clock clockParam) {
        super(TokenInspectorPortException::new);

        this.tokenTtl = tokenTtlParams;
        this.clock = clockParam;

        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate an access token for the user.
     * @param user user to generate the access token for.
     * @return access token.
     */
    @Override
    public String generateAccessToken(final User user) {
        return guard(() -> Jwts.builder()
                .subject(user.getId().toString())
                .claim("ID", user.getId())
                .issuedAt(Date.from(clock.instant()))
                .expiration(
                        Date.from(clock.instant().plus(tokenTtl)))
                .signWith(key)
                .compact(),
                "Error occurred while generating access token.");
    }

    /**
     * Extract the user id from the token.
     * @param token token to extract the user id from.
     * @return user id.
     */
    @Override
    public Optional<UUID> extractUserId(final String token) {
        return guard(() -> performExtractUserId(token),
                "Error occurred while extracting user id from token.");
    }

    /**
     * Check if the token is valid.
     * @param token token to check.
     * @return true if the token is valid, false otherwise.
     */
    @Override
    public boolean isValid(final String token) {
        return guard(()-> extractUserId(token).isPresent(),
                "Error occurred while checking if token is valid.");
    }

    /**
     * Full uuid extraction function to acomodate {@code #guard()}
     * @param token the token to extract the user id from.
     * @return user id.
     */
    private Optional<UUID> performExtractUserId(final String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .clock(() -> Date.from(clock.instant()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Optional.of(
                    UUID.fromString(claims.getSubject()));
        } catch (ExpiredJwtException e) {
            throw new TokenInvalidException(
                    "Token has expired.");
        } catch (SignatureException e) {
            throw new TokenInvalidException(
                    "Token signature is invalid.");
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new TokenInvalidException(
                    "Token format is invalid.");
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
