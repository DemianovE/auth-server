package org.demianov.auth.infrastructure.crypto;

import org.demianov.auth.main.core.application.ports.out.common.AbstractGuard;
import org.demianov.auth.main.core.application.ports.out.security.PasswordHasherPort;
import org.demianov.auth.main.core.exceptions.ports.PasswordHasherPortException;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * BCrypt password hasher.
 * <p>
 *     This is a default implementation of the {@link PasswordHasherPort}
 *     used by the application.
 * </p>
 * @since 0.1.0-alpha
 */
public class BCryptPasswordHasher
        extends AbstractGuard<PasswordHasherPortException>
        implements PasswordHasherPort {

    /**
     * Constructor.
     */
    public BCryptPasswordHasher() {
        super(PasswordHasherPortException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String hash(final String raw) {
        return guard(() -> BCrypt.hashpw(raw, BCrypt.gensalt()),
                "Error occurred while hashing the password.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify(final String raw, final String hash) {
        return guard(() -> BCrypt.checkpw(raw, hash),
                "Error occurred while verifying the password.");
    }
}
