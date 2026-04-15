package org.demianov.auth.infrastructure.persistence.jpa.repository;

import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaRefreshTokenEntity;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for the refresh token entity.
 * <p>
 *     This repository is meant to be used for the refresh token entity.
 *     It controls the default behaviour of JPA operations.
 * </p>
 * @since 0.1.0-alpha
 */
@Repository
public class JpaRefreshTokenRepository
        extends AbstractJpaRepository<JpaRefreshTokenEntity, UUID> {

    /**
     * Not args constructor.
     */
    public JpaRefreshTokenRepository() {
        super(JpaRefreshTokenEntity.class);
    }

    /**
     * Perform a search operation by refresh token.
     * @param token  refresh token.
     * @return  refresh token.
     */
    @Transactional
    public Optional<JpaRefreshTokenEntity> findByToken(
            final String token) {
        return getEntityManager().createQuery(
                "SELECT r FROM JpaRefreshTokenEntity r WHERE r.token = :token",
                        JpaRefreshTokenEntity.class)
                .setMaxResults(1)
                .setParameter("token", token)
                .getResultStream()
                .findFirst();
    }

    /**
     * Perform a broad search operation by user id.
     * @param userId user id.
     * @return list of refresh tokens.
     */
    @Transactional
    public List<JpaRefreshTokenEntity> findByUserId(
            final UUID userId) {
        return getEntityManager().createQuery(
                "SELECT r FROM JpaRefreshTokenEntity r "
                        + "WHERE r.userId = :userId",
                        JpaRefreshTokenEntity.class)
                .setParameter("userId", userId)
                .getResultStream()
                .toList();
    }

    /**
     * Perform a delete operation by refresh token.
     * @param token refresh token to be deleted.
     */
    @Transactional
    public void delete(final String token) {
        getEntityManager().createQuery(
                "DELETE FROM JpaRefreshTokenEntity r WHERE r.token = :token")
                .setParameter("token", token)
                .executeUpdate();

        getEntityManager().clear();
    }

    /**
     * Perform a broad delete operation by user id.
     * @param userId user id.
     */
    @Transactional
    public void deleteByUser(final UUID userId) {
        getEntityManager().createQuery(
                "DELETE FROM JpaRefreshTokenEntity r WHERE r.userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();

        getEntityManager().clear();
    }

    /**
     * Override the default save method.
     * @param entity entity to be saved or updated
     */
    @Override
    public void save(final JpaRefreshTokenEntity entity) {
        if (getEntityManager().contains(entity)) {
            throw new IllegalStateException(
                    "Updates are not allowed for this entity");
        }
        super.save(entity);
    }
}
