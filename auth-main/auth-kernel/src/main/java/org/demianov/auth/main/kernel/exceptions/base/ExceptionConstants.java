package org.demianov.auth.main.kernel.exceptions.base;

/**
 * Internal constant for exception handling within the base package.
 * <p>
 *     This class is package-private and should not be available outside
 *     base exceptions. It defines the HTTP status codes for each
 *     exception base case.
 * </p>
 * @since 0.1.0-alpha
 */
final class ExceptionConstants {

    static final class ErrorCodes {
        /**
         * HTTP status codes for bad requests.
         * {@link WrongParametersException}
         */
        static final int BAD_REQUEST = 400;

        /**
         * HTTP status codes for a not found entity.
         * {@link EntityNotFoundException}
         */
        static final int NOT_FOUND = 404;

        /**
         * HTTP status codes for an internal server error.
         * {@link SystemErrorException}
         */
        static final int INTERNAL_SERVER_ERROR = 500;
    }

    static final class ErrorTag {
        /**
         * Error tag for bad requests.
         * {@link WrongParametersException}
         */
        static final String BAD_REQUEST = "WRONG_PARAMETERS";

        /**
         * Error tag for a not found entity.
         * {@link EntityNotFoundException}
         */
        static final String NOT_FOUND = "NOT_FOUND";

        /**
         * Error tag for an internal server error.
         * {@link SystemErrorException}
         */
        static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    }

    /**
     * Private constructor. Prevent instantiation.
     */
    private ExceptionConstants() { }
}
