package org.demianov.auth.main.kernel.exceptions.base;


/**
 * The base exception for the authentification platform.
 * <p>
 *     THis class serves as the root for all domain and infrastructure
 *     exceptions, providing all the necessary data for mapping to a
 *     user-friendly API response.
 * </p>
 * @since 0.1.0-alpha
 */
 abstract class PlatformException extends RuntimeException {
    /**
     * The error tag like AUTH_001, AUTH_002, etc. For quick debugging.
     */
    private final String tag;
    /**
     * Status code for the return response.
     */
    private final int status;

    /**
     * Canonical Constructor available only to base exceptions.
     * @param message - error message.
     * @param errorTag - error tag.
     * @param statusCode - status code.
     */
    protected PlatformException(
            final String message,
            final String errorTag,
            final int statusCode) {
        super(message);
        this.tag = errorTag;
        this.status = statusCode;
    }

    /**
     * Get the error code.
     * @return error code.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Get the status code.
     * @return status code.
     */
    public int getStatus() {
        return status;
    }
}
