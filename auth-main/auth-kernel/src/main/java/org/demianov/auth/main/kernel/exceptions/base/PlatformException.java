package org.demianov.auth.main.kernel.exceptions.base;


/**
 * The base exception for the authentification platform.
 * <p>
 *     THis class serves as the root for all domain and infrastructure exceptions, providing all the
 *     necessary data for mapping to a user-friendly API response.
 * </p>
 * @since 0.1.0-alpha
 */
public abstract class PlatformException extends RuntimeException{
    /**
     * The error code like AUTH_001, AUTH_002, etc. For quick debugging.
     */
    private final String errorCode;
    /**
     * Status code for the return response.
     */
    private final int status;

    protected PlatformException(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    /**
     * Get the error code.
     * @return error code.
     */
    public String getErrorCode() { return errorCode; }

    /**
     * Get the status code.
     * @return status code.
     */
    public int getStatus() { return status; }
}
