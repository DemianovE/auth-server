package org.demianov.auth.infrastructure.persistence.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.demianov.auth.infrastructure.persistence.jpa.config.BaseJpaConfig;
import org.demianov.auth.infrastructure.persistence.jpa.config.JpaPersistenceConfig;
import org.demianov.auth.infrastructure.persistence.jpa.config.JpaPropertiesConfig;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaUserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        JpaRefreshTokenRepositoryTest.TestConfig.class,
        JpaPropertiesConfig.class,
        JpaPersistenceConfig.class
})
@TestPropertySource(properties = {
        "auth.repository.user.type=jpa",
        "auth.jpa.database-platform-dialect=org.hibernate.dialect.H2Dialect",
        "auth.jpa.dll-auto=create-drop"
})
public class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepository repository;

    @Configuration
    @EnableTransactionManagement
    static class TestConfig extends BaseJpaConfig {

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("org.h2.Driver");
            ds.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
            return ds;
        }
    }

    @Test
    @Transactional
    @DisplayName("Should find user by email")
    void findByEmail_Success() {
        JpaUserEntity entity = createEntity();

        this.repository.save(entity);

        Optional<JpaUserEntity> found = this.repository.findByEmail(entity.getEmail());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(entity.getId());
    }

    @Test
    @Transactional
    @DisplayName("Should save the user")
    void save_Success() {
        UUID id = UUID.randomUUID();
        JpaUserEntity entity = createEntity(id);

        this.repository.save(entity);

        Optional<JpaUserEntity> saved = this.repository.findById(id);
        assertThat(saved).isPresent();
        assertThat(saved.get().getEmail()).isEqualTo(entity.getEmail());
    }

    @Test
    @Transactional
    @DisplayName("Should delete the user by the id")
    void delete_Success() {
        UUID id = UUID.randomUUID();
        JpaUserEntity entity = createEntity(id);

        this.repository.save(entity);
        this.repository.delete(id);

        Optional<JpaUserEntity> deleted = this.repository.findById(id);
        assertThat(deleted).isNotPresent();
    }

    @Test
    @Transactional
    @DisplayName("Should correctly set the Audit values")
    void save_Audit_Success() {
        UUID id = UUID.randomUUID();
        JpaUserEntity entity = createEntity(id);


        this.repository.save(entity);

        Optional<JpaUserEntity> saved = this.repository.findById(id);
        assertThat(saved).isPresent();
        assertThat(saved.get().getCreatedAt()).isNotNull();
        assertThat(saved.get().getUpdatedAt()).isNull();

        JpaUserEntity change = saved.get();

        change.setEmail("new@test.org");

        this.repository.save(change);
        this.repository.flush();

        Optional<JpaUserEntity> savedNew = this.repository.findById(id);
        assertThat(savedNew).isPresent();
        assertThat(savedNew.get().getCreatedAt()).isEqualTo(change.getCreatedAt());
        assertThat(savedNew.get().getUpdatedAt()).isNotNull();
    }

    private JpaUserEntity createEntity(UUID id) {
        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(id);
        entity.setEmail("test@demianov.org");
        entity.setPasswordHash("secret");
        return entity;
    }

    private JpaUserEntity createEntity() {
        return createEntity(UUID.randomUUID());
    }
}
