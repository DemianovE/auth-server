package org.demianov.auth.main.core.application.services.domain_services;

import org.demianov.auth.main.kernel.application.models.UserSummary;

import org.demianov.auth.main.core.application.ports.out.persistence.UserRepoPort;
import org.demianov.auth.main.core.application.ports.out.security.TokenInspectorPort;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.models.token.TokenInvalidException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TokenShieldServiceTest {

    @Mock private TokenInspectorPort tokenInspector;
    @Mock private UserRepoPort userRepo;

    @InjectMocks private TokenShieldService service;

    @Test
    @DisplayName("Should find token successfully")
    void verifyAndIdentify_Success() {
        String token = "token-123";
        UUID userId = UUID.randomUUID();

        User user = mock(User.class);
        UserSummary userSummary = mock(UserSummary.class);

        when(this.tokenInspector.extractUserId(token)).thenReturn(Optional.of(userId));
        when(this.userRepo.findById(userId)).thenReturn(Optional.of(user));

        when(user.isActive()).thenReturn(true);
        when(user.toSummary()).thenReturn(userSummary);

        UserSummary actual = this.service.verifyAndIdentify(token);

        assertThat(actual).isEqualTo(userSummary);
        verify(this.tokenInspector, times(1)).extractUserId(token);
        verify(this.userRepo, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should throw TokenInvalidException for invalid token")
    void verifyAndIdentify_TokenInvalidException() {
        String invalidToken = "invalid-token";

        when(this.tokenInspector.extractUserId(invalidToken)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.service.verifyAndIdentify(invalidToken))
                .isInstanceOf(TokenInvalidException.class);


    }
}
