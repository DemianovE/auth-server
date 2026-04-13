package org.demianov.auth.main.core.application.ports.out.security;

import org.demianov.auth.main.core.application.models.TokenPair;
import org.demianov.auth.main.core.application.ports.in.login.LoginInputPort;
import org.demianov.auth.main.core.application.ports.out.persistence.RefreshTokenRepoPort;

import org.demianov.auth.main.core.domain.models.User;

import org.demianov.auth.main.core.exceptions.models.token.TokenNotFoundException;
import org.demianov.auth.main.core.exceptions.models.user.UserNotFoundException;
import org.demianov.auth.main.core.exceptions.ports.SecureStringGeneratorPortException;
import org.demianov.auth.main.core.exceptions.ports.TokenInspectorPortException;
import org.demianov.auth.main.core.exceptions.DataAccessException;

/**
 * Port used to abstract the whole token process.
 * <p>
 *     The port acts as bridge between more specific
 * {@link
 * RefreshTokenRepoPort
 * RefreshTokenRepoPort}
 *     and {@link TokenInspectorPort}
 *     and the {@link LoginInputPort}.
 *     To be a point in the system when the concepts are combined
 *     for generation of the token pair purposes.
 * </p>
 * <p>
 *     This port doesn't have the port-specific exception
 *     as the functions should wrap the specific exceptions.
 * </p>
 * @since 0.1.0-alpha
 */
public interface TokenGeneratorPort {

    /**
     * Perform new token pair generation.
     * <p>
     *     Process performs the generation of the new pair of tokens.
     *     As well as achieving the refresh token saving.
     *     Note that the Success record performs null-checks on its components
     * </p>
     * @param user user to generate the token pair for.
     * @throws DataAccessException if any error occurs on
     *  an infrastructure level.
     * @throws TokenInspectorPortException if any error occurs on
     * in {@link TokenInspectorPort} level.
     * @throws SecureStringGeneratorPortException if any error occurs on
     * in {@link SecureStringGeneratorPort} level.
     * @return DTO of the generated token pair.
     */
    TokenPair generate(User user);

    /**
     * Perform refresh token generation.
     * <p>
     *     The process checks and deletes the old refresh token,
     *     replacing it with a new pair of tokens.
     * </p>
     * <p>
     *     This function throws the specific exception, including
     *     those potentially thrown during the token generation phase
     *     {@link #generate(User)}.
     * </p>
     * <p>
     *     The flow first creates a new pair of tokens, then deletes it.
     * </p>
     * @param tokenValue the refresh token to be used for refreshing.
     * @throws DataAccessException if any error occurs on in frustum level.
     * @throws TokenNotFoundException if the token is not found.
     * @throws UserNotFoundException if the user is not found.
     * @return new token pair in DTO.
     */
    TokenPair refresh(String tokenValue);
}
