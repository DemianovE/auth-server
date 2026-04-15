package org.demianov.auth.infrastructure.persistence.jpa.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JpaRefreshTokenEntityTest {

    @Test
    @DisplayName("Should return true when passed same object to equals")
    void equals_Success_SameObject() {
        JpaRefreshTokenEntity entity = createEntity();

        assertTrue(entity.equals(entity));
    }

    @Test
    @DisplayName("Should return false when passed different type of object to equals")
    void equals_Fail_DifferentType() {
        JpaRefreshTokenEntity entity = createEntity();
        Object other = new Object();

        assertFalse(entity.equals(other));
    }

    @Test
    @DisplayName("Should return false when passed object with different id to equals")
    void equals_Fail_DifferentId() {
        JpaRefreshTokenEntity entity = createEntity();
        JpaRefreshTokenEntity other = createEntity();
        other.setId(UUID.randomUUID());

        assertFalse(entity.equals(other));
    }

    @Test
    @DisplayName("Should return hash made from id")
    void hashCode_Success() {
        JpaRefreshTokenEntity entity = createEntity();
        int hashCode = entity.hashCode();
        assertEquals(Objects.hash(entity.getId()), hashCode);
    }

    public JpaRefreshTokenEntity createEntity() {
        JpaRefreshTokenEntity entity = new JpaRefreshTokenEntity();
        entity.setToken("token");
        entity.setId(UUID.randomUUID());
        entity.setUserId(UUID.randomUUID());
        entity.setExpiry(Instant.now().plus(Duration.ofDays(1)));
        return entity;
    }
}
