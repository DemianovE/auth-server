package org.demianov.auth.main.core.application.models;

import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

import java.time.Duration;
import java.util.Objects;

/**
 * Sealed interface for a Login use-case result.
 * <p>
 *     Represent different states of a result
 *     possible as an output of the Login use-case.
 * </p>
 *
 * <p>
 *     The interface has the following states:
 *     <ul>
 *         <li>{@link LoginResult.Success} - to handle
 *              successfull flow return</li>
 *         <li>{@link LoginResult.Failure} - for handling
 *              the use-case's exceptions</li>
 *     </ul>
 * </p>
 *
 * @see org.demianov.auth.main.core.application.ports.in.login.LoginInputPort
 * @since 0.1.0-alpha
 */
public sealed interface LoginResult {

    /**
     * Represents a successful login result.
     *
     * <p>
     *     Contains the generated access token, refresh token,
     *     access token expiration time, and refresh token expiration time.
     * </p>
     *
     * <p>
     *     These results represent the successful login flow. As such it is
     *     directly map to API response.
     * </p>
     * @param accessToken access token.
     * @param refreshToken refresh token.
     * @param expiresIn access token expiration time.
     * @param refreshTokenTtl refresh token expiration time.
     *
     * @since 0.1.0-alpha
     */
    record Success(
            String accessToken,
            String refreshToken,

            Duration expiresIn,
            Duration refreshTokenTtl
    ) implements LoginResult {
        /**
         * Canonical constructor. Perform mandatory field validation.
         * @throws NullPointerException if any of the fields is null.
         */
        public Success {
            Objects.requireNonNull(accessToken,
                    "accessToken cannot be null");
            Objects.requireNonNull(refreshToken,
                    "refreshToken cannot be null");
            Objects.requireNonNull(expiresIn,
                    "expiresIn cannot be null");
            Objects.requireNonNull(refreshTokenTtl,
                    "refreshTokenTtl cannot be null");
        }
    }

    /**
     * Represents a failed login result.
     * <p>
     *     Contains the exception that caused the failure.
     *     This exception is required for the error to API response mapping.
     * </p>
     *
     * <p>
     *     A specific tag {@link LoginPortExceptions} is used to
     *     mark all exceptions that are thrown in the port layer.
     * </p>
     * @param exception - the exception that caused the failure.
     *
     * @since 0.1.0-alpha
     */
    record Failure(LoginPortExceptions exception) implements LoginResult {

        /**
         * Canonical constructor. Perform mandatory field validation.
         * @throws NullPointerException if the exception is null.
         */
        public Failure {
            Objects.requireNonNull(exception,
                "exception cannot be null");
        }
    }
}
