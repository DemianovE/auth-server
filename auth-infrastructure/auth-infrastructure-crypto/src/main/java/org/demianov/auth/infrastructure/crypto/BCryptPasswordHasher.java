package org.demianov.auth.infrastructure.crypto;

import org.demianov.auth.main.core.application.ports.out.security.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * BCrypt password hasher.
 * <p>
 *     This is a default implementation of the {@link PasswordHasherPort}
 *     used by the application.
 * </p>
 * @since 0.1.0-alpha
 */
public class BCryptPasswordHasher implements PasswordHasherPort {

    /**
     * Hash the raw password.
     * @param raw  raw password String.
     * @return BCrypt hashed password.
     */
    @Override
    public String hash(final String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt());
    }

    /**
     * Verify the raw password against the hashed password.
     * @param raw raw password String.
     * @param hash hashed password.
     * @return true if the raw password matches the hashed password.
     */
    @Override
    public boolean verify(final String raw, final String hash) {
        return BCrypt.checkpw(raw, hash);
    }
}
