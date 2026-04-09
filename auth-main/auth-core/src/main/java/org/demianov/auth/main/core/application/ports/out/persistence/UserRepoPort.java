package org.demianov.auth.main.core.application.ports.out.persistence;

import org.demianov.auth.main.core.domain.models.User;

import org.demianov.auth.main.kernel.domain.models.Email;

import java.util.Optional;
import java.util.UUID;

/**
 * Port to work with the user repository.
 * <p>
 *     Port to abstract functionality related to
 *     the user repository.
 * </p>
 * @see User
 * @since 0.1.0-alpha
 */
public interface UserRepoPort {
    /**
     * Perform user search by email.
     * @param email users' email.
     * @return user with the given email.
     * @throws org.demianov.auth.main.core.exceptions.DataAccessException
     * if any error occurs during the search process.
     */
    Optional<User> findByEmail(Email email);

    /**
     * Perform user search by id.
     * @param id users' id.
     * @return user with the given id.
     * @throws org.demianov.auth.main.core.exceptions.DataAccessException
     * if any error occurs during the search process.
     */
    Optional<User> findById(UUID id);

    /**
     * Perform user object save.
     * @param user - user to be saved.
     * @throws org.demianov.auth.main.core.exceptions.DataAccessException
     * if any error occurs during the saving process.
     */
    void save(User user);

    /**
     * Perform user object deletion.
     * @param user - user to be deleted.
     * @throws org.demianov.auth.main.core.exceptions.DataAccessException
     * if any error occurs during the deletion process.
     */
    void delete(User user);
}
