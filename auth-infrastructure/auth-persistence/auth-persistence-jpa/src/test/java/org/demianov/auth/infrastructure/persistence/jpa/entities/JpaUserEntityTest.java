package org.demianov.auth.infrastructure.persistence.jpa.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JpaUserEntityTest {

    @Test
    @DisplayName("Should return true when the same object is passed to equals")
    void equals_Success_SameObject() {
        JpaUserEntity entity = createEntity();

        assertTrue(entity.equals(entity));
    }

    @Test
    @DisplayName("Should return false when object is not JpaUserEntity")
    void equals_Fail_DifferentType() {
        JpaUserEntity entity = createEntity();
        Object other = new Object();

        assertFalse(entity.equals(other));
    }

    @Test
    @DisplayName("Should return false when id is not the same")
    void equals_Fail_DifferentId() {
        JpaUserEntity entity = createEntity();
        JpaUserEntity other = createEntity();
        other.setId(UUID.randomUUID());

        assertFalse(entity.equals(other));
    }

    @Test
    @DisplayName("Should return hash made from id")
    void hashCode_Success() {
        JpaUserEntity entity = createEntity();
        int hashCode = entity.hashCode();
        assertEquals(Objects.hash(entity.getId()), hashCode);
    }

    private JpaUserEntity createEntity() {
        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(UUID.randomUUID());
        entity.setEmail("test@demianov.org");
        entity.setPasswordHash("hashedPass");

        return entity;
    }
}
