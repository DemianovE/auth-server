package org.demianov.auth.infrastructure.persistence.jpa.config.keys;

/**
 * Holder of default property keys.
 * <p>
 *     These keys are used to define default values for the properties.
 * </p>
 * @since 0.1.0-alpha
 */
public final class AuthPropertyKeys {
    // || JPA DLL ||
    /** JPA properties. */
    public static final String JPA_DLL_AUTO =
            "auth.jpa.dll-auto";
    /** Default value for the JPA DLL auto property. */
    public static final String JPA_DEFAULT_DLL_AUTO =
            "update";

    // || JPA DIALECT ||
    /** JPA database platform dialect. */
    public static final String JPA_DATABASE_PLATFORM_DIALECT =
            "auth.jpa.database-platform-dialect";
    /** Default value for the JPA database platform dialect property. */
    public static final String JPA_DEFAULT_DATABASE_PLATFORM_DIALECT =
            "org.hibernate.dialect.PostgreSQLDialect";

    /** Private constructor. Prevent instantiation. */
    private AuthPropertyKeys() {
    }
}
