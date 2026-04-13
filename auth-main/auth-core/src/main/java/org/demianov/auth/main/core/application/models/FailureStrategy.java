package org.demianov.auth.main.core.application.models;

/**
 * The strategy to use when a failure occurs.
 * <p>
 *     The strategy determines how the system should handle failures.
 *     This applies to {@code CriticalLoginHandler} and
 *     {@code LoginListener}
 * </p>
 * @since 0.1.0-alpha
 */
public enum FailureStrategy {
    /** Stop execution immediately after the first failure. */
    FAIL_FAST,
    /** Ignore the failure and continue with the next handler. */
    CONTINUE_ON_FAIL
}
