package org.demianov.auth.infrastructure.persistence.jpa.mapper;

import org.demianov.auth.main.core.domain.models.RefreshToken;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaRefreshTokenEntity;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaUserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class JpaRefreshTokenMapperTest {

    @Test
    @DisplayName("Should successfully create entity")
    void toEntity_Success() {
        RefreshToken token = new RefreshToken(
                UUID.randomUUID(),
                "token",
                UUID.randomUUID(),
                Instant.now()
        );


        JpaRefreshTokenEntity entity = JpaRefreshTokenMapper.toEntity(token);

        assertThat(entity.getId()).isEqualTo(token.id());
        assertThat(entity.getToken()).isEqualTo(token.token());
        assertThat(entity.getUserId()).isEqualTo(token.userId());
        assertThat(entity.getExpiry()).isEqualTo(token.expiry());
    }

    @Test
    @DisplayName("Should successfully create domain")
    void toDomain_Success() {

        JpaRefreshTokenEntity entity = new JpaRefreshTokenEntity();
        entity.setId(UUID.randomUUID());
        entity.setToken("token");
        entity.setUserId(UUID.randomUUID());
        entity.setExpiry(Instant.now());

        RefreshToken token = JpaRefreshTokenMapper.toDomain(entity);

        assertThat(token.id()).isEqualTo(entity.getId());
        assertThat(token.token()).isEqualTo(entity.getToken());
        assertThat(token.userId()).isEqualTo(entity.getUserId());
        assertThat(token.expiry()).isEqualTo(entity.getExpiry());
    }

    @Test
    @DisplayName("toDomain Should return null when given one")
    void toDomain_Null() {
        assertThat(JpaRefreshTokenMapper.toDomain(null)).isNull();
    }

    @Test
    @DisplayName("toEntity Should return null when given one")
    void toEntity_Null() {
        assertThat(JpaRefreshTokenMapper.toEntity(null)).isNull();
    }
}
