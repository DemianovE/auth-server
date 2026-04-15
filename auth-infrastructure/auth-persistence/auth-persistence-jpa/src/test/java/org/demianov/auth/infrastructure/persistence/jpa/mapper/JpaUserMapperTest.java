package org.demianov.auth.infrastructure.persistence.jpa.mapper;

import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaUserEntity;
import org.demianov.auth.main.kernel.domain.models.Email;
import org.demianov.auth.main.kernel.domain.models.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class JpaUserMapperTest {

    @Test
    @DisplayName("Should successfully create entity")
    void toEntity_Success() {
        User user = User.builder(UUID.randomUUID(), Email.of("test@demianov.org"), "password")
                .status(UserStatus.ACTIVE)
                .build();

        JpaUserEntity entity = JpaUserMapper.toEntity(user);

        assertThat(entity.getId()).isEqualTo(user.getId());
        assertThat(entity.getEmail()).isEqualTo(user.getEmail().value());
        assertThat(entity.getStatus()).isEqualTo(user.getStatus());
        assertThat(entity.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("Should successfully create model")
    void toDomain_Success() {
        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(UUID.randomUUID());
        entity.setEmail("test@demianov.org");
        entity.setPasswordHash("hashedPass");
        entity.setStatus(UserStatus.ACTIVE);

        User domain = JpaUserMapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(entity.getId());
        assertThat(domain.getEmail().value()).isEqualTo(entity.getEmail());
        assertThat(domain.getPasswordHash()).isEqualTo(entity.getPasswordHash());
        assertThat(domain.getStatus()).isEqualTo(entity.getStatus());
        assertThat(domain.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("toDomain Should return null when given one")
    void toDomain_Null() {
        assertThat(JpaUserMapper.toDomain(null)).isNull();
    }

    @Test
    @DisplayName("toEntity Should return null when given one")
    void toEntity_Null() {
        assertThat(JpaUserMapper.toEntity(null)).isNull();
    }

}
