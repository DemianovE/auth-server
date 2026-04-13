package org.demianov.auth.main.kernel.api;

/**
 * Interface for prioritized objects.
 * <p>
 *     Can be used to prioritize objects in the system.
 *     Like the {@code @Order} annotation in Spring.
 * </p>
 * <p>
 *     The default priority is 0.
 * </p>
 * @since 0.1.0-alpha
 */
public interface Prioritized {

    /**
     * The priority of the object.
     * @return the priority.
     */
    default int getPriority() {
        return 0;
    }
}
