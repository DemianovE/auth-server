package org.demianov.auth.infrastructure.persistence.jpa.config.keys;

/**
 * The class contains all keys and default values for the auth tables.
 * @since 0.1.0-alpha
 */
public final class AuthTableKeys {

    // || TABLE PREFIX ||
    /** Table prefix. */
    public static final String PREFIX = "auth.jpa.table-prefix";
    /** Default value for the table prefix property. */
    public static final String PREFIX_DEFAULT = "auth_";

    // || TABLE USERS ||
    /** Table users. */
    public static final String USERS = "auth.jpa.table.users";
    /** Default value for the table users property. */
    public static final String USERS_DEFAULT = "users";

    // || TABLE REFRESH TOKENS ||
    /** Table refresh tokens. */
    public static final String REFRESH_TOKENS
            = "auth.jpa.table.refresh-tokens";
    /** The default value for the table refresh tokens property. */
    public static final String REFRESH_TOKENS_DEFAULT = "refresh_tokens";

    /** Private constructor. Prevent instantiation. */
    private AuthTableKeys() {
    }
}
