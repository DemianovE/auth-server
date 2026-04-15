package org.demianov.auth.infrastructure.persistence.jpa.repository;

import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaUserEntity;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for the user entity.
 * <p>
 *     This repository is meant to be used for the user entity.
 *     It controls the default behaviour of JPA operations.
 * </p>
 * @since 0.1.0-alpha
 */
@Repository
public class JpaUserRepository extends
        AbstractJpaRepository<JpaUserEntity, UUID> {

    /**
     * Not args constructor.
     */
    public JpaUserRepository() {
        super(JpaUserEntity.class);
    }

    /**
     * Perform an operation to find the user by email.
     * <p>
     *     Will return the first user found.
     *     If no user is found, the result will be empty.
     * </p>
     * @param email  user email.
     * @return  user with the given email.
     */
    @Transactional
    public Optional<JpaUserEntity> findByEmail(
            final String email) {
        return getEntityManager().createQuery(
                "SELECT u FROM JpaUserEntity u WHERE u.email = :email",
                JpaUserEntity.class)
                .setMaxResults(1)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }
}
