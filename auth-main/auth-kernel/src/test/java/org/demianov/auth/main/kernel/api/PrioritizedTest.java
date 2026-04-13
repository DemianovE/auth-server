package org.demianov.auth.main.kernel.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedTest {

    @Test
    void testDefaultPriority() {
        Prioritized prioritized = new TestPrioritized();

        assertEquals(0, prioritized.getPriority());
    }
}
