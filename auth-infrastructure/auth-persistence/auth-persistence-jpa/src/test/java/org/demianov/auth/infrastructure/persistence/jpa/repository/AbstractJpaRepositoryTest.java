package org.demianov.auth.infrastructure.persistence.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.demianov.auth.infrastructure.persistence.jpa.entities.AbstractAuditEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("Should ignore exception when entity to delete does not exist")
    void delete_ShouldIgnorePersistenceException() {
        TestRepo repo = new TestRepo();
        EntityManager emMock = mock(EntityManager.class);

        ReflectionTestUtils.setField(repo, "entityManager", emMock);

        UUID randomId = UUID.randomUUID();
        when(emMock.getReference(any(), any()))
                .thenThrow(new PersistenceException("Simulated failure"));

        assertDoesNotThrow(() -> repo.delete(randomId));

        verify(emMock).getReference(eq(TestEntity.class), eq(randomId));
    }

    @Test
    @DisplayName("Should throw exception when not PersistenceException")
    void delete_Failure() {
        String message = "Simulated failure";
        TestRepo repo = new TestRepo();
        EntityManager emMock = mock(EntityManager.class);

        ReflectionTestUtils.setField(repo, "entityManager", emMock);

        UUID randomId = UUID.randomUUID();
        when(emMock.getReference(any(), any()))
                .thenThrow(new IncompatibleClassChangeError(message));

        assertThatThrownBy(() -> repo.delete(randomId))
                .isInstanceOf(IncompatibleClassChangeError.class)
                .hasMessage(message);

        verify(emMock).getReference(eq(TestEntity.class), eq(randomId));
    }
}
