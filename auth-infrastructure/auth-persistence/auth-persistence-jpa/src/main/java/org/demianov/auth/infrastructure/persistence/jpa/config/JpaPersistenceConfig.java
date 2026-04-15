package org.demianov.auth.infrastructure.persistence.jpa.config;

import org.demianov.auth.infrastructure.persistence.jpa.adapters.JpaRefreshTokenAdapter;
import org.demianov.auth.infrastructure.persistence.jpa.adapters.JpaUserAdapter;
import org.demianov.auth.infrastructure.persistence.jpa.config.conditions.JpaRefreshTokenCondition;
import org.demianov.auth.infrastructure.persistence.jpa.config.conditions.JpaUserCondition;
import org.demianov.auth.infrastructure.persistence.jpa.repository.JpaRefreshTokenRepository;
import org.demianov.auth.infrastructure.persistence.jpa.repository.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The JPA persistence configuration.
 * <p>
 *     The configuration is responsible for the JPA persistence layer.
 *     It initiated all Beans required for the JPA operations.
 * </p>
 * @since 0.1.0-alpha
 */
@Configuration
@EnableTransactionManagement
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class JpaPersistenceConfig {

    /**
     * The JPA user configuration.
     */
    @Configuration
    @Conditional(JpaUserCondition.class)
    static class JpaUserConfig {
        /**
         * The JPA user repository.
         * @return the JPA user repository.
         */
        @Bean
        public JpaUserRepository jpaUserRepository() {
            return new JpaUserRepository();
        }

        /**
         * The JPA user adapter.
         * @param jpaUserRepository the JPA user repository.
         * @return the JPA user adapter.
         */
        @Bean
        public JpaUserAdapter jpaUserAdapter(
                final JpaUserRepository jpaUserRepository) {
            return new JpaUserAdapter(jpaUserRepository);
        }
    }

    /**
     * The JPA refresh token configuration.
     */
    @Configuration
    @Conditional(JpaRefreshTokenCondition.class)
    static class JpaRefreshTokenConfig {
        /**
         * The JPA refresh token repository.
         * @return the JPA refresh token repository.
         */
        @Bean
        public JpaRefreshTokenRepository jpaRefreshTokenRepository() {
            return new JpaRefreshTokenRepository();
        }

        /**
         * The JPA refresh token adapter.
         * @param jpaRefreshTokenRepository the JPA refresh token repository.
         * @return the JPA refresh token adapter.
         */
        @Bean
        public JpaRefreshTokenAdapter jpaRefreshTokenAdapter(
                final JpaRefreshTokenRepository jpaRefreshTokenRepository) {
            return new JpaRefreshTokenAdapter(jpaRefreshTokenRepository);
        }
    }
}
