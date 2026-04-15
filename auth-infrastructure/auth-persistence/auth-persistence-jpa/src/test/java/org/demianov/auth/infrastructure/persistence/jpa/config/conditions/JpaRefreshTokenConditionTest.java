package org.demianov.auth.infrastructure.persistence.jpa.config.conditions;

import org.junit.jupiter.api.BeforeEach;

public class JpaRefreshTokenConditionTest extends AbstractConditionTest<JpaRefreshTokenCondition> {

    @BeforeEach
    void setUp() {
        super.setUp(new JpaRefreshTokenCondition(), "auth.repository.refresh-token.type");
    }
}
