package org.demianov.auth.main.core.application.services.use_case.login;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.ports.out.security.TokenGeneratorPort;
import org.demianov.auth.main.core.application.services.use_case.login.internal.DefaultLoginDispatcher;
import org.demianov.auth.main.core.domain.models.User;

import org.demianov.auth.main.core.exceptions.HandlerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultLoginDispatcherTest {

    @Mock private TokenGeneratorPort tokenGenerator;

    @Test
    @DisplayName("Should return new AuthResult")
    void dispatch_Success() {
        User user = mock(User.class);

        LoginResult.Success result = new LoginResult.Success(
                "access",
                "refresh",
                Duration.ofMinutes(1000),
                Duration.ofMinutes(1000)
        );

        when(this.tokenGenerator.generate(user)).thenReturn(result);

        DefaultLoginDispatcher dispatcher = new DefaultLoginDispatcher(this.tokenGenerator, List.of());
        LoginResult actual = dispatcher.dispatch(user);

        assertThat(actual).isEqualTo(result);
    }

    @Test
    @DisplayName("Should replace null handlers with empty list")
    void dispatch_NullHandlers() {
        TokenGeneratorPort tokenGeneratorMock = mock(TokenGeneratorPort.class);
        User user = mock(User.class);

        DefaultLoginDispatcher dispatcher = new DefaultLoginDispatcher(
                tokenGeneratorMock,
                null
        );

        assertDoesNotThrow(() -> {
            dispatcher.dispatch(user);
        }, "Dispatcher should handle null handlers list by treating it as empty.");

        verify(tokenGeneratorMock, times(1)).generate(user);
    }

    @Test
    @DisplayName("Should run all handlers")
    void dispatch_AllHandlers() {
        User user = mock(User.class);
        LoginHandler handler1 = mock(LoginHandler.class);
        LoginHandler handler2 = mock(LoginHandler.class);

        DefaultLoginDispatcher dispatcher = new DefaultLoginDispatcher(this.tokenGenerator, List.of(handler1, handler2));
        dispatcher.dispatch(user);

        verify(handler1, times(1)).handle(user);
        verify(handler2, times(1)).handle(user);
    }

    @Test
    @DisplayName("Should return wrapped HandlerException")
    void dispatch_HandlerException() {
        User user = mock(User.class);
        LoginHandler handler = mock(LoginHandler.class);

        String message = "Handler exception";
        Throwable cause = new RuntimeException(message);

        doThrow(new HandlerException(message, cause)).when(handler).handle(user);

        LoginResult result = new DefaultLoginDispatcher(this.tokenGenerator, List.of(handler)).dispatch(user);

        assertThat(result).isInstanceOf(LoginResult.Failure.class)
                .asInstanceOf(InstanceOfAssertFactories.type(LoginResult.Failure.class))
                .satisfies(failure -> assertThat(failure.exception()).isInstanceOf(HandlerException.class)
                        .asInstanceOf(InstanceOfAssertFactories.type(HandlerException.class))
                        .satisfies(handlerException -> {
                            assertThat(handlerException.getMessage()).isEqualTo(message);
                            assertThat(handlerException.getCause()).isEqualTo(cause);
                            assertThat(handlerException.getCause().getMessage()).isEqualTo(cause.getMessage());
                        }));
    }
}
