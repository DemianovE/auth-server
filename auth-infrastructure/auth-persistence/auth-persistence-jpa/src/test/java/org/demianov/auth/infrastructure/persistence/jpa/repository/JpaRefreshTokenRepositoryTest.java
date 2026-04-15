package org.demianov.auth.infrastructure.persistence.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.assertj.core.api.AssertionsForClassTypes;
import org.demianov.auth.infrastructure.persistence.jpa.config.BaseJpaConfig;
import org.demianov.auth.infrastructure.persistence.jpa.config.JpaPersistenceConfig;
import org.demianov.auth.infrastructure.persistence.jpa.config.JpaPropertiesConfig;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaRefreshTokenEntity;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaUserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        JpaRefreshTokenRepositoryTest.TestConfig.class,
        JpaPropertiesConfig.class,
        JpaPersistenceConfig.class
})
@TestPropertySource(properties = {
        "auth.repository.refresh-token.type=jpa",
        "auth.jpa.database-platform-dialect=org.hibernate.dialect.H2Dialect",
        "auth.jpa.dll-auto=create-drop"
})
public class JpaRefreshTokenRepositoryTest {

    @Autowired
    private JpaRefreshTokenRepository repository;

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
    @DisplayName("Should find token by token string")
    void findByToken_Success() {
        JpaRefreshTokenEntity entity = createEntity();

        this.repository.save(entity);

        Optional<JpaRefreshTokenEntity> found = this.repository.findByToken(entity.getToken());

        assertThat(found).isPresent();
        assertThat(found.get().getToken()).isEqualTo(entity.getToken());
    }

    @Test
    @Transactional
    @DisplayName("Should find tokens by user id")
    void findByUserId_Success() {
        JpaRefreshTokenEntity entity = createEntity();

        this.repository.save(entity);

        List<JpaRefreshTokenEntity> found = this.repository.findByUserId(entity.getUserId());

        assertThat(found).contains(entity);
    }

    @Test
    @Transactional
    @DisplayName("Should find token by the token id")
    void findById_Success() {
        UUID id = UUID.randomUUID();
        JpaRefreshTokenEntity entity = createEntity(id);

        this.repository.save(entity);

        Optional<JpaRefreshTokenEntity> found = this.repository.findById(id);

        assertThat(found).isPresent();
        assertThat(found.get().getToken()).isEqualTo(entity.getToken());
    }
    @Test
    @Transactional
    @DisplayName("Should delete the token by the token string")
    void delete_token_Success() {
        UUID id = UUID.randomUUID();
        JpaRefreshTokenEntity entity = createEntity(id);

        this.repository.save(entity);

        this.repository.delete(entity.getToken());

        Optional<JpaRefreshTokenEntity> deleted = this.repository.findById(id);
        assertThat(deleted).isNotPresent();
    }

    @Test
    @Transactional
    @DisplayName("Should delete token by user id")
    void deleteByUser_Success() {
        UUID id = UUID.randomUUID();
        JpaRefreshTokenEntity entity = createEntity(id);

        this.repository.save(entity);

        this.repository.deleteByUser(entity.getUserId());

        Optional<JpaRefreshTokenEntity> deleted = this.repository.findById(id);
        assertThat(deleted).isNotPresent();
    }

    @Test
    @Transactional
    @DisplayName("Should delete token by id")
    void deleteById_Success() {
        UUID id = UUID.randomUUID();
        JpaRefreshTokenEntity entity = createEntity(id);

        this.repository.save(entity);

        this.repository.delete(id);

        Optional<JpaRefreshTokenEntity> deleted = this.repository.findById(id);
        assertThat(deleted).isNotPresent();
    }

    @Test
    @Transactional
    @DisplayName("Should save the token")
    void save_Success() {
        UUID id = UUID.randomUUID();
        JpaRefreshTokenEntity entity = createEntity(id);

        this.repository.save(entity);

        Optional<JpaRefreshTokenEntity> saved = this.repository.findById(id);
        assertThat(saved).isPresent();
        assertThat(saved.get().getToken()).isEqualTo(entity.getToken());
    }

    @Test
    @Transactional
    @DisplayName("Should delete the token by the token id")
    void delete_Success() {
        UUID id = UUID.randomUUID();
        JpaRefreshTokenEntity entity = createEntity(id);

        this.repository.save(entity);

        repository.delete(id);

        Optional<JpaRefreshTokenEntity> deleted = this.repository.findById(id);
        assertThat(deleted).isNotPresent();
    }

    @Test
    @Transactional
    @DisplayName("Should correctly set the Audit values")
    void save_Audit_Success() {
        UUID id = UUID.randomUUID();
        JpaRefreshTokenEntity entity = createEntity(id);

        this.repository.save(entity);

        Optional<JpaRefreshTokenEntity> saved = this.repository.findById(id);
        assertThat(saved).isPresent();
        assertThat(saved.get().getCreatedAt()).isNotNull();
        assertThat(saved.get().getUpdatedAt()).isNull();
    }

    @Test
    @Transactional
    @DisplayName("Should not allow update of entity")
    void update_Forbidden() {
        UUID id = UUID.randomUUID();
        JpaRefreshTokenEntity entity = createEntity(id);

        this.repository.save(entity);

        Optional<JpaRefreshTokenEntity> saved = this.repository.findById(id);
        assertThat(saved).isPresent();

        JpaRefreshTokenEntity specificSave = saved.get();
        specificSave.setToken("newToken");

        assertThatThrownBy(() -> repository.save(specificSave))
                .isInstanceOf(IllegalStateException.class);
    }

    /**
     * The default entity creator.
     * @return the entity.
     */
    private JpaRefreshTokenEntity createEntity(UUID id) {
        JpaRefreshTokenEntity entity = new JpaRefreshTokenEntity();
        entity.setToken("token");
        entity.setUserId(UUID.randomUUID());
        entity.setId(id);
        entity.setExpiry(null);
        return entity;
    }

    private JpaRefreshTokenEntity createEntity(){
        return createEntity(UUID.randomUUID());
    }
}
