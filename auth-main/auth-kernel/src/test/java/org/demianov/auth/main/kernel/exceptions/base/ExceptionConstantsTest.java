package org.demianov.auth.main.kernel.exceptions.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionConstantsTest {

    @Test
    @DisplayName("Verify status codes are correct")
    void statusCodes_Success() {
        assertThat(ExceptionConstants.ErrorCodes.BAD_REQUEST).isEqualTo(400);
        assertThat(ExceptionConstants.ErrorCodes.UNAUTHORIZED).isEqualTo(401);
        assertThat(ExceptionConstants.ErrorCodes.FORBIDDEN).isEqualTo(403);
        assertThat(ExceptionConstants.ErrorCodes.NOT_FOUND).isEqualTo(404);
        assertThat(ExceptionConstants.ErrorCodes.INTERNAL_SERVER_ERROR).isEqualTo(500);
    }

    @Test
    @DisplayName("Verify tags are correct")
    void tags_Success() {
        assertThat(ExceptionConstants.ErrorTag.BAD_REQUEST).isEqualTo("BAD_REQUEST");
        assertThat(ExceptionConstants.ErrorTag.UNAUTHORIZED).isEqualTo("UNAUTHORIZED");
        assertThat(ExceptionConstants.ErrorTag.FORBIDDEN).isEqualTo("FORBIDDEN");
        assertThat(ExceptionConstants.ErrorTag.NOT_FOUND).isEqualTo("NOT_FOUND");
        assertThat(ExceptionConstants.ErrorTag.INTERNAL_SERVER_ERROR).isEqualTo("INTERNAL_SERVER_ERROR");
    }
}
