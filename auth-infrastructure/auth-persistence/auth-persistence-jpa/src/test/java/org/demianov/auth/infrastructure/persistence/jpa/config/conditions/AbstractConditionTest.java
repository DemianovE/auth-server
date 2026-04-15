package org.demianov.auth.infrastructure.persistence.jpa.config.conditions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractConditionTest <T extends Condition>{
    private T condition;
    private String property;

    private final ConditionContext context = mock(ConditionContext.class);
    private final AnnotatedTypeMetadata metadata = mock(AnnotatedTypeMetadata.class);
    private final MockEnvironment environment = new MockEnvironment();

    void setUp(T condition, String property) {
        this.condition = condition;
        this.property = property;
        when(context.getEnvironment()).thenReturn(environment);
    }

    @Test
    @DisplayName("Should match when property is jpa")
    void match_Success() {
        this.environment.setProperty(this.property, "jpa");

        assertTrue(this.condition.matches(this.context, this.metadata));
    }

    @Test
    @DisplayName("Should not match when property is not jpa")
    void shouldNotMatchWhenPropertyIsRedis() {
        this.environment.setProperty(this.property, "redis");

        assertFalse(this.condition.matches(this.context, this.metadata));
    }

    @Test
    @DisplayName("Should not match when property is missing")
    void match_Fail_PropertyMissing() {
        assertFalse(this.condition.matches(this.context, this.metadata));
    }

    @Test
    @DisplayName("Should match when property is JPA")
    void match_Success_CaseInsensitive() {
        this.environment.setProperty(this.property, "JPA");

        assertTrue(this.condition.matches(this.context, this.metadata));
    }
}
