package org.demianov.auth.main.core.application.ports.out.persistence;

import org.demianov.auth.main.core.application.ports.out.security.TokenGeneratorPort;

import org.demianov.auth.main.core.domain.models.RefreshToken;
import org.demianov.auth.main.core.domain.models.User;

import org.demianov.auth.main.core.exceptions.DataAccessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port to work with the refresh token repository.
 * <p>
 *     Port to abstract functionality related to the
 *     refresh token repository. The repository is further
 *     abstracted by
 *     {@link TokenGeneratorPort}
 *     removing the need to work directly with the repository.
 * </p>
 * @since 0.1.0-alpha
 */
public interface RefreshTokenRepoPort {

    /**
     * Function to perform object saving.
     * @param token refresh token to be saved.
     * @throws DataAccessException
     * if any error occurs during the saving process.
     */
    void save(RefreshToken token);

    /**
     * Perform a refresh token search by its token id.
     * @param tokenValue refresh token actual value.
     * @return refresh token.
     * @throws DataAccessException
     * if any error occurs during the search process.
     */
    Optional<RefreshToken> findByToken(String tokenValue);

    /**
     * Perform an all-token search by users' id.
     * @param userId if of the tokens'
     * {@link User}.
     * @return list of refresh tokens of user.
     * @throws DataAccessException
     * if any error occurs during the search process.
     */
    List<RefreshToken> findByUserId(UUID userId);

    /**
     * Perform a refresh token search by its id.
     * @param tokenId - refresh token id.
     * @return - refresh token.
     * @throws DataAccessException
     * if any error occurs during the search process.
     */
    Optional<RefreshToken> findById(UUID tokenId);

    /**
     * Perform a refresh token deletion.
     * @param token - refresh token to be deleted.
     * @throws DataAccessException
     * if any error occurs during the deletion process.
     */
    void delete(RefreshToken token);

    /**
     * Perform a refresh token deletion.
     * @param tokenValue - refresh token actual value.
     * @throws DataAccessException
     * if any error occurs during the deletion process.
     */
    void delete(String tokenValue);

    /**
     * Perform a refresh token deletion.
     * @param userId - the id of tokens'
     * {@link User}.
     * @throws DataAccessException
     * if any error occurs during the deletion process.
     */
    void delete(UUID userId);
}
