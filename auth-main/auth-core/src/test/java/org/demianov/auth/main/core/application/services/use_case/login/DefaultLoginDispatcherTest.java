package org.demianov.auth.main.core.application.services.use_case.login;

import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.models.TokenPair;
import org.demianov.auth.main.core.domain.models.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Well, the default impl does nothing, so
 * this is a <i>bissi</i> useless
 */
@ExtendWith(MockitoExtension.class)
public class DefaultLoginDispatcherTest {

    @Test
    @DisplayName("Should return new AuthResult")
    void dispatch_Success() {
        User user = mock(User.class);

        TokenPair tokens = new TokenPair(
                "access",
                "refresh",
                Duration.ofMinutes(1000),
                Duration.ofMinutes(1000)
        );

        LoginResult.Success result = new LoginResult.Success(
                tokens.accessToken(),
                tokens.refreshToken(),
                tokens.expiresIn(),
                tokens.refreshTokenTtl()
        );

        DefaultLoginDispatcher dispatcher = new DefaultLoginDispatcher();
        LoginResult actual = dispatcher.dispatch(result, user);

        assertThat(actual).isEqualTo(result);
    }
}
