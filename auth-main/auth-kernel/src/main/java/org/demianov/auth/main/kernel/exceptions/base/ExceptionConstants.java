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
         * @see WrongParametersException
         */
        static final int BAD_REQUEST = 400;

        /**
         * HTTP status codes for a not found entity.
         * @see EntityNotFoundException
         */
        static final int NOT_FOUND = 404;

        /**
         * HTTP status codes for an internal server error.
         * @see SystemErrorException
         */
        static final int INTERNAL_SERVER_ERROR = 500;

        /**
         * HTTP status codes for an Unauthorized error.
         * @see AuthenticationFailureException
         */
        static final int UNAUTHORIZED = 401;

        /**
         * HTTP status codes for an Forbidden error.
         * @see ActionForbiddenException
         */
        static final int FORBIDDEN = 403;
    }

    static final class ErrorTag {
        /**
         * Error tag for bad requests.
         * @see WrongParametersException
         */
        static final String BAD_REQUEST = "BAD_REQUEST";

        /**
         * Error tag for a not found entity.
         * @see EntityNotFoundException
         */
        static final String NOT_FOUND = "NOT_FOUND";

        /**
         * Error tag for an internal server error.
         * @see SystemErrorException
         */
        static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

        /**
         * Error tag for an Unauthorized error.
         * @see AuthenticationFailureException
         */
        static final String UNAUTHORIZED = "UNAUTHORIZED";

        /**
         * Error tag for an Forbidden error.
         * @see ActionForbiddenException
         */
        static final String FORBIDDEN = "FORBIDDEN";
    }

    /**
     * Private constructor. Prevent instantiation.
     */
    private ExceptionConstants() { }
}
