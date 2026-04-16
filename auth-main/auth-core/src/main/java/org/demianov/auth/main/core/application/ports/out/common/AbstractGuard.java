package org.demianov.auth.main.core.application.ports.out.common;

import org.demianov.auth.main.core.exceptions.AuthCoreException;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Abstract class for the guard pattern.
 * @param <E> exception type
 * @since 0.1.0-alpha
 */
public abstract class AbstractGuard<E extends AuthCoreException> {

    /** Factory for creating the exception. */
    private final BiFunction<String, Throwable, E> exceptionFactory;

    /**
     * Constructor.
     * @param exceptionFactoryParam factory for creating the exception.
     */
    protected AbstractGuard(
            final BiFunction<String, Throwable, E> exceptionFactoryParam) {
        this.exceptionFactory = exceptionFactoryParam;
    }

    /**
     * Guard method.
     * @param action the action to be performed.
     * @param errorMessage the error message.
     * @param <T> the return type.
     * @return the result of the action.
     */
    protected <T> T guard(
            final Supplier<T> action,
            final String errorMessage) {
        try {
            return action.get();
        } catch (Exception e) {
            throw exceptionFactory.apply(errorMessage, e);
        }
    }

    /**
     * Guard method.
     * @param action the action to be performed.
     * @param errorMessage the error message.
     */
    protected void guard(
            final Runnable action,
            final String errorMessage) {
        try {
            action.run();
        } catch (Exception e) {
            throw exceptionFactory.apply(errorMessage, e);
        }
    }
}
