package org.demianov.auth.infrastructure.persistence.jpa.mapper;

import org.demianov.auth.main.core.domain.models.RefreshToken;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaRefreshTokenEntity;

/**
 * The mapper used for the Domain|Entity
 * mapping of the RefreshToken domain model.
 * @see RefreshToken
 * @see JpaRefreshTokenEntity
 * @since 1.0.0
 */
public final class JpaRefreshTokenMapper {

    /**
     * Private constructor.
     * @implNote This class is a utility class and should not be instantiated.
     */
    private JpaRefreshTokenMapper() {
    }

    /**
     * Maps a JPA entity to a domain model.
     * @param entity - JPA entity
     * @return - domain model
     */
    public static RefreshToken toDomain(final JpaRefreshTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        return new RefreshToken(
                entity.getId(),
                entity.getToken(),
                entity.getUserId(),
                entity.getExpiry()
        );
    }

    /**
     * Maps a domain model to a JPA entity.
     * @param model - domain model
     * @return - JPA entity
     */
    public static JpaRefreshTokenEntity toEntity(final RefreshToken model) {
        if (model == null) {
            return null;
        }

        JpaRefreshTokenEntity entity = new JpaRefreshTokenEntity();
        entity.setId(model.id());
        entity.setToken(model.token());

        entity.setUserId(model.userId());
        entity.setExpiry(model.expiry());

        return entity;
    }
}
