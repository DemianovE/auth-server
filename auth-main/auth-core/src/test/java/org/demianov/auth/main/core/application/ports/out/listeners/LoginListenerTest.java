package org.demianov.auth.main.core.application.ports.out.listeners;

import org.demianov.auth.main.core.application.models.LoginCommand;
import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.kernel.domain.models.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class LoginListenerTest {

    @Test
    @DisplayName("onFailure should accept LoginCommand without throwing exception")
    void onFailureShouldAcceptCommand() {
        LoginListener listener = spy(new LoginListener() {
            @Override
            public void onSuccess(User user) {
            }
        });

        LoginCommand command = new LoginCommand(Email.of("test@demianov.org"), "testPass");

        listener.onFailure(command);

        verify(listener, times(1)).onFailure(command);
    }
}
