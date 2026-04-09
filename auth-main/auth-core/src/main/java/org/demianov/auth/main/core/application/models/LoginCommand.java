package org.demianov.auth.main.core.application.models;

import org.demianov.auth.main.kernel.domain.models.Email;

import java.util.Objects;

/**
 * Domain model used to store the Login Command inner DTO.
 * <p>
 *     This DTO stores the user's email and password for the login
 *     use-case process. It auto validates the email and password.
 * </p>
 * @param email user email
 * @param password user password
 *
 * @see org.demianov.auth.main.core.application.ports.in.login.LoginInputPort
 * @since 0.1.0-alpha
 */
public record LoginCommand(
        Email email,
        String password
) {

    /**
     * Canonical constructor. Perform mandatory field validation.
     * @param email user email
     * @param password user password
     * @throws NullPointerException if any of the fields is null.
     */
    public LoginCommand {
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(password, "password cannot be null");
    }
}
