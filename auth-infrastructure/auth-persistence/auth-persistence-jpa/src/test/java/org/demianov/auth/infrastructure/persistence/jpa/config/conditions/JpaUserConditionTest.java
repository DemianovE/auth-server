package org.demianov.auth.infrastructure.persistence.jpa.config.conditions;

import org.junit.jupiter.api.BeforeEach;

public class JpaUserConditionTest extends AbstractConditionTest<JpaUserCondition> {

    @BeforeEach
    void setUp() {
        super.setUp(new JpaUserCondition(), "auth.repository.user.type");
    }
}
