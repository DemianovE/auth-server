package org.demianov.auth.infrastructure.persistence.jpa.config;

import org.demianov.auth.infrastructure.persistence.jpa.config.keys.AuthPropertyKeys;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * The base JPA configuration.
 * <p>
 *     The configuration is responsible for the JPA persistence layer.
 *     It is generic and can be used for any JPA implementation.
 * </p>
 * @since 0.1.0-alpha
 */
@Configuration
@Import(JpaPropertiesConfig.class)
public abstract class BaseJpaConfig {

    /**
     * The implementation of the JPA entity manager factory.
     * <p>
     *     As this is a generic configuration, a data source
     *     is required to be provided by the developer.
     * </p>
     * @param dataSource the data source of the database.
     * @param env the environment.
     * @return the JPA entity manager factory.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final DataSource dataSource,
            final Environment env) {
        LocalContainerEntityManagerFactoryBean em =
                new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);

        em.setPackagesToScan(
                "org.demianov.auth.infrastructure.persistence.jpa.entities");

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        String dialect = env.getProperty(
                AuthPropertyKeys.JPA_DATABASE_PLATFORM_DIALECT,
                AuthPropertyKeys.JPA_DEFAULT_DATABASE_PLATFORM_DIALECT);

        String ddlAuto = env.getProperty(
                AuthPropertyKeys.JPA_DLL_AUTO,
                AuthPropertyKeys.JPA_DEFAULT_DLL_AUTO);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);

        properties.put(
                "hibernate.physical_naming_strategy",
                new AuthPhysicalNamingStrategy(env));
        em.setJpaProperties(properties);

        return em;
    }

    /**
     * The transaction manager.
     * @param emf EntityManagerFactory implementation Bean.
     * @return the transaction manager.
     */
    @Bean
    public PlatformTransactionManager transactionManager(
            final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
