package org.demianov.auth.infrastructure.persistence.jpa.config;

import org.demianov.auth.infrastructure.persistence.jpa.config.keys.AuthTableKeys;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.core.env.Environment;

public final class AuthPhysicalNamingStrategy
        extends PhysicalNamingStrategyStandardImpl {

    /** The table prefix. */
    private final String tablePrefix;
    /** The environment used to get properties. */
    private final Environment env;

    /**
     * Constructor. Using env to get table prefix and suffixes.
     * @param envParam the environment.
     */
    public AuthPhysicalNamingStrategy(final Environment envParam) {
        this.env = envParam;
        this.tablePrefix = env.getProperty(
                AuthTableKeys.PREFIX,
                AuthTableKeys.PREFIX_DEFAULT);

    }

    @Override
    public Identifier toPhysicalTableName(
            final Identifier name,
            final JdbcEnvironment context) {
        String logicalName = name.getText();

        String mappingBase = "auth.jpa.table.mapping.";
        String propertyKey = mappingBase + logicalName;
        String mappedName = this.env.getProperty(propertyKey, logicalName);

        return Identifier.toIdentifier(this.tablePrefix + mappedName);
    }
}
