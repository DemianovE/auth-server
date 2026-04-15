package org.demianov.auth.infrastructure.persistence.jpa.adapters;

import org.demianov.auth.infrastructure.persistence.jpa.mapper.JpaRefreshTokenMapper;
import org.demianov.auth.main.core.domain.models.RefreshToken;
import org.demianov.auth.infrastructure.persistence.jpa.entities.JpaRefreshTokenEntity;
import org.demianov.auth.infrastructure.persistence.jpa.repository.JpaRefreshTokenRepository;
import org.demianov.auth.main.core.exceptions.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class JpaRefreshTokenAdapterTest {

    @Mock private JpaUserAdapter userAdapter;
    @Mock private JpaRefreshTokenRepository repository;

    @InjectMocks
    private JpaRefreshTokenAdapter adapter;

    private JpaRefreshTokenEntity entity;

    @BeforeEach
    void setUp() {
        this.entity = new JpaRefreshTokenEntity();
        this.entity.setToken("token");
        this.entity.setId(UUID.randomUUID());
        this.entity.setExpiry(Instant.now());
        this.entity.setUserId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Should find token by token string")
    void findByToken_Success() {

        when(this.repository.findByToken("token")).thenReturn(Optional.of(this.entity));

        Optional<RefreshToken> token = this.adapter.findByToken("token");

        assertThat(token).isPresent();
        assertThat(token.get().token()).isEqualTo(this.entity.getToken());
        assertThat(token.get().userId()).isEqualTo(this.entity.getUserId());
        assertThat(token.get().expiry()).isEqualTo(this.entity.getExpiry());
        verify(this.repository, times(1)).findByToken("token");
    }

    @Test
    @DisplayName("Should not find token by token string")
    void findByToken_NotFound() {
        when(this.repository.findByToken("token")).thenReturn(Optional.empty());

        Optional<RefreshToken> token = this.adapter.findByToken("token");

        assertThat(token).isNotPresent();
        verify(this.repository, times(1)).findByToken("token");
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens searching by token string")
    void findByToken_DataAccessException() {
        String message = "Database error";
        Throwable cause = new RuntimeException(message);
        when(this.repository.findByToken("token")).thenThrow(new RuntimeException(message, cause));

        assertThatThrownBy(() -> this.adapter.findByToken("token"))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred while searching for token by token value: " + "token")
                .hasCause(cause);
    }

    @Test
    @DisplayName("Should find token by user id")
    void findByUserId_Success() {
        JpaRefreshTokenEntity otherToken = new JpaRefreshTokenEntity();
        otherToken.setToken("other-token");
        otherToken.setId(UUID.randomUUID());
        otherToken.setExpiry(Instant.now());
        otherToken.setUserId(this.entity.getUserId());

        when(this.repository.findByUserId(this.entity.getId())).thenReturn(List.of(this.entity, otherToken));

        List<RefreshToken> token = this.adapter.findByUserId(this.entity.getId());

        RefreshToken expectedToken = new RefreshToken(
                this.entity.getId(),
                this.entity.getToken(),
                this.entity.getUserId(),
                this.entity.getExpiry());

        assertThat(token)
                .hasSize(2)
                .contains(expectedToken);
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens searching by user id")
    void findByUserId_DataAccessException() {
        String message = "Database error";
        Throwable cause = new RuntimeException(message);
        when(this.repository.findByUserId(this.entity.getId())).thenThrow(new RuntimeException(message, cause));

        assertThatThrownBy(() -> this.adapter.findByUserId(this.entity.getId()))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred while searching for tokens by user id: " + this.entity.getId())
                .hasCause(cause);
    }

    @Test
    @DisplayName("Should find token by token id")
    void findById_Success() {
        when(this.repository.findById(this.entity.getId())).thenReturn(Optional.of(this.entity));

        Optional<RefreshToken> token = this.adapter.findById(this.entity.getId());

        assertThat(token).isPresent();
        assertThat(token.get().token()).isEqualTo(this.entity.getToken());
        assertThat(token.get().userId()).isEqualTo(this.entity.getUserId());
        assertThat(token.get().expiry()).isEqualTo(this.entity.getExpiry());
    }

    @Test
    @DisplayName("Should not find token by token id")
    void findById_NotFound() {
        when(this.repository.findById(this.entity.getId())).thenReturn(Optional.empty());

        Optional<RefreshToken> token = this.adapter.findById(this.entity.getId());

        assertThat(token).isNotPresent();
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens searching by token id")
    void findById_DataAccessException() {
        String message = "Database error";
        Throwable cause = new RuntimeException(message);
        when(this.repository.findById(this.entity.getId())).thenThrow(new RuntimeException(message, cause));

        assertThatThrownBy(() -> this.adapter.findById(this.entity.getId()))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred while searching for token by id: " + this.entity.getId())
                .hasCause(cause);
    }

    @Test
    @DisplayName("Should save token")
    void save_Success() {
        RefreshToken token = JpaRefreshTokenMapper.toDomain(this.entity);
        doNothing().when(this.repository).save(this.entity);
        this.adapter.save(token);
        verify(this.repository, times(1)).save(this.entity);
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens saving token")
    void save_DataAccessException() {
        RefreshToken token = JpaRefreshTokenMapper.toDomain(this.entity);
        doThrow(new RuntimeException("Database error")).when(this.repository).save(this.entity);
        assertThatThrownBy(() -> this.adapter.save(token))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred during saving of the token: " + token.id());
    }

    @Test
    @DisplayName("Should delete token")
    void delete_Success() {
        RefreshToken token = JpaRefreshTokenMapper.toDomain(this.entity);
        doNothing().when(this.repository).delete(this.entity.getId());
        this.adapter.delete(token);
        verify(this.repository, times(1)).delete(this.entity.getId());
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens deleting token")
    void delete_DataAccessException() {
        RefreshToken token = JpaRefreshTokenMapper.toDomain(this.entity);
        doThrow(new RuntimeException("Database error")).when(this.repository).delete(this.entity.getId());
        assertThatThrownBy(() -> this.adapter.delete(token))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred during deleting of the token: " + token.id());
    }

    @Test
    @DisplayName("Should delete token by token string")
    void delete_Success_ByToken() {
        doNothing().when(this.repository).delete(this.entity.getToken());
        this.adapter.delete(this.entity.getToken());
        verify(this.repository, times(1)).delete(this.entity.getToken());
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens deleting token by token string")
    void delete_DataAccessException_ByToken() {
        doThrow(new RuntimeException("Database error")).when(this.repository).delete(this.entity.getToken());
        assertThatThrownBy(() -> this.adapter.delete(this.entity.getToken()))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred during deleting of the token by token value: " + this.entity.getToken());
    }

    @Test
    @DisplayName("Should delete tokens by userId")
    void delete_Success_ByUserId() {
        doNothing().when(this.repository).deleteByUser(this.entity.getUserId());
        this.adapter.delete(this.entity.getUserId());
        verify(this.repository, times(1)).deleteByUser(this.entity.getUserId());
    }

    @Test
    @DisplayName("Should throw DataAccessException if any error is thrown whens deleting tokens by userId")
    void delete_DataAccessException_ByUserId() {
        doThrow(new RuntimeException("Database error")).when(this.repository).deleteByUser(this.entity.getUserId());
        assertThatThrownBy(() -> this.adapter.delete(this.entity.getUserId()))
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred during deleting of the tokens by user id: " + this.entity.getUserId());
    }
}
