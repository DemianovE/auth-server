package org.demianov.auth.main.core.application.ports.out.security;

import org.demianov.auth.main.core.exceptions.ports.PasswordHasherPortException;

/**
 * Security port for password hashing method.
 * <p>
 *     Used to represent the desired password hashing method.
 * </p>
 * <p>
 *     The port has a port-specific exception
 *     {@link PasswordHasherPortException}.
 *     The exception should be used by developers to handle
 *     exceptions specific to the port implementation.
 * </p>
 * @since 0.1.0-alpha
 */
public interface PasswordHasherPort {

    /**
     * Perform hashing on the raw password.
     * @param raw  raw password String.
     * @return  hashed password.
     * @throws PasswordHasherPortException
     * if any error occurs during the hashing process.
     */
    String hash(String raw);

    /**
     * Perform the password verification against the hashed one.
     * @param raw raw password String.
     * @param hash hashed password.
     * @return true if the raw password matches the hashed one,
     * false otherwise.
     * @throws PasswordHasherPortException
     * if any error occurs during the verification process.
     */
    boolean verify(String raw, String hash);
}
