package org.demianov.auth.infrastructure.persistence.jpa.config.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

/**
 * Condition to check if the JPA user repository is enabled.
 */
public class JpaUserCondition implements Condition {

    /**
     * Checks if the JPA user repository is enabled.
     * @param context the condition context
     * @param metadata the metadata of the {@code AnnotationMetadata class}
     * or {@code MethodMetadata method} being checked
     * @return {@code true} if the JPA user repository is enabled,
     * {@code false} otherwise
     */
    @Override
    public boolean matches(
            final ConditionContext context,
            final @NonNull AnnotatedTypeMetadata metadata) {
        String type = context.getEnvironment()
                .getProperty("auth.repository.user.type");
        return "jpa".equalsIgnoreCase(type);
    }
}
