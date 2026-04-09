package org.demianov.auth.main.core.application.ports.out.security;


import org.demianov.auth.main.core.exceptions.ports.SecureStringGeneratorPortException;

/**
 * Port to work with secure string generation.
 * <p>
 *     Abstracts the generation of secure strings.
 *     Mainly used in the token generation process.
 * </p>
 * <p>
 *     The port has a port-specific exception
 *     {@link SecureStringGeneratorPortException}.
 *     The exception should be used by developers to handle
 *     exceptions specific to the port implementation.
 * </p>
 * @since 0.1.0-alpha
 */
public interface SecureStringGeneratorPort {

    /**
     * Perform secure string generation.
     * @return - generated secure string.
     * @throws SecureStringGeneratorPortException
     * if any error occurs during the generation process.
     */
    String generate();
}
