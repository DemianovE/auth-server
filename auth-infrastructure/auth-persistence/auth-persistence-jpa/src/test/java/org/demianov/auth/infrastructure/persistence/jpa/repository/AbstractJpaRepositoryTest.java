package org.demianov.auth.infrastructure.persistence.jpa.repository;

import org.demianov.auth.infrastructure.persistence.jpa.entities.AbstractAuditEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractJpaRepositoryTest {

    private static class TestEntity extends AbstractAuditEntity {}
    private static class TestRepo extends AbstractJpaRepository<TestEntity, UUID> {
        public TestRepo() {
            super(TestEntity.class);
        }

        public Class<TestEntity> testGetEntityClass() {
            return getEntityClass();
        }
    }

    @Test
    @DisplayName("Should correctly set entity via constructor")
    void constructor_Success() {
        TestRepo repo = new TestRepo();
        assertEquals(TestEntity.class, repo.testGetEntityClass());
    }
}
