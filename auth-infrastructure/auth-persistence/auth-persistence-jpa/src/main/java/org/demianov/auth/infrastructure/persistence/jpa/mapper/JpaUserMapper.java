package org.demianov.auth.infrastructure.persistence.jpa.mapper;

import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaUserEntity;
import org.demianov.auth.main.kernel.domain.models.Email;

/**
 * The mapper used for the Domain|Entity
 * mapping of the User domain model.
 * <p>
 *     The mapper is responsible for converting between
 *     the domain model and the JPA entity.
 * </p>
 * @see User
 * @see JpaUserEntity
 * @since 0.1.0-alpha
 */
public final class JpaUserMapper {

    /**
     * Private constructor.
     * @implNote This class is a utility class and should not be instantiated.
     */
    private JpaUserMapper() {
    }

    /**
     * Maps a JPA entity to a domain model.
     * @param entity - JPA entity
     * @return - domain model
     */
    public static User toDomain(final JpaUserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.rehydrate(
                entity.getId(),
                Email.of(entity.getEmail()),
                entity.getPasswordHash(),
                entity.getStatus(),
                entity.getRoles()
        );
    }

    /**
     * Maps a domain model to a JPA entity.
     * @param model - domain model
     * @return - JPA entity
     */
    public static JpaUserEntity toEntity(final User model) {
        if (model == null) {
            return null;
        }

        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(model.getId());
        entity.setEmail(model.getEmail().value());
        entity.setPasswordHash(model.getPasswordHash());
        entity.setStatus(model.getStatus());
        entity.setRoles(model.getRoles());
        return entity;
    }
}
