package org.demianov.auth.infrastructure.persistence.jpa.adapters;

import lombok.AllArgsConstructor;
import org.demianov.auth.main.core.application.ports.out.persistence.RefreshTokenRepoPort;
import org.demianov.auth.main.core.domain.models.RefreshToken;
import org.demianov.auth.infrastructure.persistence.jpa.mapper.JpaRefreshTokenMapper;
import org.demianov.auth.infrastructure.persistence.jpa.repository.JpaRefreshTokenRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA Adapter for the RefreshToken domain model.
 * <p>
 *     The adapter is responsible for converting between
 *     the domain model and the JPA entity.
 * </p>
 * @see RefreshToken
 * @see JpaRefreshTokenRepository
 * @since 0.1.0-alpha
 */
@AllArgsConstructor
public final class JpaRefreshTokenAdapter
        extends AbstractJpaAdapter
        implements RefreshTokenRepoPort {

    /** The JPA refresh token repository. */
    private final JpaRefreshTokenRepository jpaRepository;

    @Override
    public Optional<RefreshToken> findByToken(final String token) {
        return guard(() -> jpaRepository.findByToken(token)
                .map(JpaRefreshTokenMapper::toDomain),
                "Error occurred while searching for token by token value: "
                        + token);
    }

    @Override
    public List<RefreshToken> findByUserId(final UUID userId) {
        return guard(() -> jpaRepository.findByUserId(userId)
                .stream()
                .map(JpaRefreshTokenMapper::toDomain)
                .toList(),
                "Error occurred while searching for tokens by user id: "
                        + userId);
    }

    @Override
    public Optional<RefreshToken> findById(final UUID tokenId) {
        return guard(() -> jpaRepository.findById(tokenId)
                .map(JpaRefreshTokenMapper::toDomain),
                "Error occurred while searching for token by id: "
                        + tokenId);
    }

    @Override
    public void save(final RefreshToken token) {
        guard(() -> jpaRepository.save(JpaRefreshTokenMapper.toEntity(token)),
                "Error occurred during saving of the token: "
                        + token.id());
    }

    @Override
    public void delete(final RefreshToken token) {
        guard(() -> jpaRepository.delete(token.id()),
                "Error occurred during deleting of the token: "
                        + token.id());
    }

    @Override
    public void delete(final String token) {
        guard(() -> jpaRepository.delete(token),
                "Error occurred during deleting of the "
                        + "token by token value: " + token);
    }

    @Override
    public void delete(final UUID userId) {
        guard(() -> jpaRepository.deleteByUser(userId),
                "Error occurred during deleting of the"
                        + " tokens by user id: " + userId);
    }
}
