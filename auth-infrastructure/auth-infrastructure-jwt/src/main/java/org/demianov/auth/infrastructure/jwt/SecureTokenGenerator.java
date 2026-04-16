package org.demianov.auth.infrastructure.jwt;

import org.demianov.auth.main.core.application.ports.out.security.SecureStringGeneratorPort;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Secure token generator.
 */
public class SecureTokenGenerator implements SecureStringGeneratorPort {
    /** Secure random generator. */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    /** Base64 encoder. */
    private static final Base64.Encoder ENCODER =
            Base64.getUrlEncoder().withoutPadding();
    /** The length of the generated token. */
    private final int byteLength;

    /** Default token length. */
    private static final int DEFAULT_TOKEN_LENGTH = 32;

    /**
     * Constructor. The default length is 32 bytes.
     */
    public SecureTokenGenerator() {
        this(DEFAULT_TOKEN_LENGTH);
    }

    /**
     * Constructor.
     * @param byteLengthParam 16, 24, 32
     */
    public SecureTokenGenerator(final int byteLengthParam) {
        this.byteLength = byteLengthParam;
    }

    /**
     * Generate a secure random string.
     * @return secure random string.
     */
    @Override
    public String generate() {
        byte[] randomBytes = new byte[byteLength];
        SECURE_RANDOM.nextBytes(randomBytes);
        return ENCODER.encodeToString(randomBytes);
    }
}
