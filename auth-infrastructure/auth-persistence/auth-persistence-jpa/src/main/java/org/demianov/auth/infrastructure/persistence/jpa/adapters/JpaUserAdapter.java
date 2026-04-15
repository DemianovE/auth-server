package org.demianov.auth.infrastructure.persistence.jpa.adapters;

import lombok.AllArgsConstructor;
import org.demianov.auth.main.core.application.ports.out.persistence.UserRepoPort;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.infrastructure.persistence.jpa.mapper.JpaUserMapper;
import org.demianov.auth.infrastructure.persistence.jpa.repository.JpaUserRepository;
import org.demianov.auth.main.kernel.domain.models.Email;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA Adapter for the User domain model.
 * <p>
 *     The adapter is responsible for converting between
 *     the domain model and the JPA entity.
 * </p>
 * @see User
 * @see JpaUserRepository
 * @since 0.1.0-alpha
 */
@AllArgsConstructor
public final class JpaUserAdapter
        extends AbstractJpaAdapter
        implements UserRepoPort {

    /** The JPA user repository. */
    private final JpaUserRepository jpaRepository;

    @Override
    public Optional<User> findByEmail(final Email email) {
        return guard(() -> jpaRepository.findByEmail(email.value())
                .map(JpaUserMapper::toDomain),
                "Error occurred while searching for user by email: "
                        + email.value());
    }

    @Override
    public Optional<User> findById(final UUID id) {
        return guard(() -> jpaRepository.findById(id)
                .map(JpaUserMapper::toDomain),
                "Error occurred while searching for user by id: "
                        + id);
    }

    @Override
    public void save(final User user) {
        guard(() -> jpaRepository.save(JpaUserMapper.toEntity(user)),
                "Error occurred while saving user: "
                        + user.getId());
    }

    @Override
    public void delete(final User user) {
        guard(() -> jpaRepository.delete(user.getId()),
                "Error occurred while deleting user: "
                        + user.getId());
    }
}
