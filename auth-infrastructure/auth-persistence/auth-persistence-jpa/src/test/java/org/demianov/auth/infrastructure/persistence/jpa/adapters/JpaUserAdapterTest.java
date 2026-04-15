package org.demianov.auth.infrastructure.persistence.jpa.adapters;

import org.assertj.core.api.InstanceOfAssertFactory;
import org.demianov.auth.infrastructure.persistence.jpa.mapper.JpaUserMapper;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaUserEntity;
import org.demianov.auth.infrastructure.persistence.jpa.repository.JpaUserRepository;
import org.demianov.auth.main.core.exceptions.DataAccessException;
import org.demianov.auth.main.kernel.domain.models.Email;
import org.demianov.auth.main.kernel.domain.models.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JpaUserAdapterTest {

    @Mock
    private JpaUserRepository repository;

    @InjectMocks
    private JpaUserAdapter adapter;

    private JpaUserEntity entity;

    @BeforeEach
    void setUp() {
        this.entity = new JpaUserEntity();
        this.entity.setEmail("text@demianov.org");
        this.entity.setId(UUID.randomUUID());
        this.entity.setPasswordHash("hashedPass");
        this.entity.setStatus(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should find user by email")
    void findByEmail_Success() {

        when(this.repository.findByEmail(this.entity.getEmail())).thenReturn(Optional.of(this.entity));

        Optional<User> found = this.adapter.findByEmail(Email.of((this.entity.getEmail())));

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(this.entity.getId());
        assertThat(found.get().getEmail().value()).isEqualTo(this.entity.getEmail());
        assertThat(found.get().getPasswordHash()).isEqualTo(this.entity.getPasswordHash());
        assertThat(found.get().getStatus()).isEqualTo(this.entity.getStatus());
        verify(repository, times(1)).findByEmail(this.entity.getEmail());
    }

    @Test
    @DisplayName("Should not return user by email")
    void findByEmail_Failure() {
        String email = "test@demianov.org";
        when(this.repository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> notFound = this.adapter.findByEmail(Email.of(email));

        assertThat(notFound).isNotPresent();
        verify(this.repository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw DataAccessException when error is throw while finding by email")
    void findByEmail_DataAccessException() {
        String message = "Database error";
        Throwable cause = new RuntimeException(message);
        when(this.repository.findByEmail(this.entity.getEmail())).thenThrow(new RuntimeException(message, cause));

        assertThatThrownBy(() -> this.adapter.findByEmail(Email.of(this.entity.getEmail())))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred while searching for user by email: " + this.entity.getEmail())
                .hasCause(cause);
    }

    @Test
    @DisplayName("Should find user by user id")
    void findById_Success() {
        when(this.repository.findById(this.entity.getId())).thenReturn(Optional.of(this.entity));

        Optional<User> found = this.adapter.findById(this.entity.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(this.entity.getId());
        assertThat(found.get().getEmail().value()).isEqualTo(this.entity.getEmail());
        assertThat(found.get().getPasswordHash()).isEqualTo(this.entity.getPasswordHash());
        assertThat(found.get().getStatus()).isEqualTo(this.entity.getStatus());
        verify(repository, times(1)).findById(this.entity.getId());
    }

    @Test
    @DisplayName("Should not return user by user id")
    void findById_Failure() {
        UUID id = UUID.randomUUID();
        when(this.repository.findById(id)).thenReturn(Optional.empty());

        Optional<User> notFound = this.adapter.findById(id);

        assertThat(notFound).isNotPresent();
        verify(this.repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens searching by id")
    void findById_DataAccessException() {
        String message = "Database error";
        Throwable cause = new RuntimeException(message);
        when(this.repository.findById(this.entity.getId())).thenThrow(new RuntimeException(message, cause));

        assertThatThrownBy(() -> this.adapter.findById(this.entity.getId()))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred while searching for user by id: " + this.entity.getId())
                .hasCause(cause);
    }

    @Test
    @DisplayName("Should save user.")
    void save_Success() {
        User user = User.builder(this.entity.getId(), Email.of(this.entity.getEmail()), this.entity.getPasswordHash())
                .status(this.entity.getStatus())
                .build();

        ArgumentCaptor<JpaUserEntity> entityCaptor = ArgumentCaptor.forClass(JpaUserEntity.class);

        doNothing().when(this.repository).save(any());

        this.adapter.save(user);

        verify(this.repository, times(1)).save(entityCaptor.capture());

        JpaUserEntity capturedEntity = entityCaptor.getValue();

        assertThat(capturedEntity.getId()).isEqualTo(this.entity.getId());
        assertThat(capturedEntity.getEmail()).isEqualTo(this.entity.getEmail());
        assertThat(capturedEntity.getStatus()).isEqualTo(this.entity.getStatus());
        assertThat(capturedEntity.getPasswordHash()).isEqualTo(this.entity.getPasswordHash());
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens saving user.")
    void save_DataAccessException() {
        User user = JpaUserMapper.toDomain(this.entity);
        doThrow(new RuntimeException("Database error")).when(this.repository).save(any());
        assertThatThrownBy(() -> this.adapter.save(user))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred while saving user: " + user.getId());
    }

    @Test
    @DisplayName("Should delete user")
    void delete_Success() {
        User user = JpaUserMapper.toDomain(this.entity);
        doNothing().when(this.repository).delete(this.entity.getId());

        this.adapter.delete(user);
        verify(this.repository, times(1)).delete(this.entity.getId());
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens deleting user")
    void delete_DataAccessException() {
        User user = JpaUserMapper.toDomain(this.entity);
        doThrow(new RuntimeException("Database error")).when(this.repository).delete(this.entity.getId());

        assertThatThrownBy(() -> this.adapter.delete(user))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred while deleting user: " + user.getId());
    }
}
