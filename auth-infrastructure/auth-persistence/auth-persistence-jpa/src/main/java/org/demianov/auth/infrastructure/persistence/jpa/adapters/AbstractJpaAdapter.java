package org.demianov.auth.infrastructure.persistence.jpa.adapters;

import org.demianov.auth.main.core.exceptions.DataAccessException;

import java.util.function.Supplier;

/**
 * The abstract class for the default functionality of JPA adapters.
 * <p>
 *     Current main purpose is to host the safeguard function
 *     to perform auto conversion of any error into
 *     core expected  {@link DataAccessException}.
 * </p>
 * @since 0.1.0-alpha
 */
public abstract class AbstractJpaAdapter {

    /**
     * Not args constructor.
     */
    protected AbstractJpaAdapter() {
    }

    /**
     * Performs the safeguard function.
     * <p>
     *     The function will catch any exception during the
     *     adapter execution and convert it into {@link DataAccessException}.
     *     The {@link DataAccessException} saves the original exception
     *     as well as custom error message.
     * </p>
     * @param <T> the type of the result of the supplier.
     * @param action the action to be performed by adapter.
     * @param errorMessage the error message to be used in the exception.
     * @return the result of the supplier.
     * @throws DataAccessException if any error occurs during the execution.
     */
    protected <T> T guard(
            final Supplier<T> action,
            final String errorMessage) {
        try {
            return action.get();
        } catch (Exception e) {
            throw new DataAccessException(errorMessage, e);
        }
    }

    /**
     * Performs the safeguard function for void actions.
     * @param action the action to be performed (no return value).
     * @param errorMessage the error message.
     * @throws DataAccessException if any error occurs during the execution.
     */
    protected void guard(
            final Runnable action,
            final String errorMessage) {
        try {
            action.run();
        } catch (Exception e) {
            throw new DataAccessException(errorMessage, e);
        }
    }
}
