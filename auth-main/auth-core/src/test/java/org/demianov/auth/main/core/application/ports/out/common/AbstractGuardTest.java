package org.demianov.auth.main.core.application.ports.out.common;

import org.demianov.auth.main.core.domain.models.User;
import org.demianov.auth.main.core.exceptions.DataAccessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.mockito.Mockito.*;

public class AbstractGuardTest {

     private static class TestClass extends AbstractGuard<DataAccessException> {
         private final User user;

         public TestClass(User user) {
             super(DataAccessException::new);

             this.user = user;
         }

         public UUID performGuard() {
             return guard(user::getId,
                     "Error occurred while performing guard: ");
         }

         public void performVoidGuard() {
             guard(() -> user.authenticate(null, null),
                     "Error occurred while performing guard: ");
         }
     }

     @Test
     @DisplayName("Should correctly perform return guard")
     void performGuard_Success() {
        User user = mock(User.class);
        TestClass testClass = new TestClass(user);

        when(user.getId()).thenReturn(UUID.randomUUID());

        UUID id = testClass.performGuard();
        verify(user, times(1)).getId();
     }

     @Test
     @DisplayName("Should throw DataAccessException on guard failure")
     void performGuard_Failure() {
        User user = mock(User.class);
        TestClass testClass = new TestClass(user);
        RuntimeException cause = new RuntimeException("Database error");

        when(user.getId()).thenThrow(cause);

        assertThatThrownBy(testClass::performGuard)
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred while performing guard: ")
                .hasCause(cause);
     }

     @Test
     @DisplayName("Should correctly perform void guard")
     void performVoidGuard_Success() {
        User user = mock(User.class);
        TestClass testClass = new TestClass(user);

        doNothing().when(user).authenticate(null, null);

        testClass.performVoidGuard();
        verify(user, times(1)).authenticate(null, null);
     }

     @Test
     @DisplayName("Should throw DataAccessException on guard failure")
     void performVoidGuard_Failure() {
        User user = mock(User.class);
        TestClass testClass = new TestClass(user);
        RuntimeException cause = new RuntimeException("Database error");

        doThrow(cause).when(user).authenticate(null, null);

        assertThatThrownBy(testClass::performVoidGuard)
                .isInstanceOf(DataAccessException.class)
                .hasMessage("Error occurred while performing guard: ")
                .hasCause(cause);
     }
}
