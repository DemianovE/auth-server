package org.demianov.auth.infrastructure.persistence.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;

/**
 * The configuration for the auth properties.
 * <p>
 *     The configuration provides user-specific properties
 *     defined in the {@code .properties} file.
 *     The default properties are defined in the
 *     {@code auth-jpa-defaults.properties} file.
 * </p>
 * @since 0.1.0-alpha
 */
@Configuration
@PropertySource(value = "classpath:auth-jpa-defaults.properties",
        ignoreResourceNotFound = true)
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class JpaPropertiesConfig {

    /**
     * The property sources placeholder configurer.
     * @return new property sources placeholder configurer.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer
                propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * The persistence annotation bean post-processor.
     * @return new persistence annotation bean post-processor.
     */
    @Bean
    public PersistenceAnnotationBeanPostProcessor
                persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }
}
