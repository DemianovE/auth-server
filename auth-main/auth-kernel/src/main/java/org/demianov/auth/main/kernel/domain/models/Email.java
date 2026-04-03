package org.demianov.auth.main.kernel.domain.models;


import org.demianov.auth.main.kernel.exceptions.base.WrongParametersException;

/**
 * Represents a validated email address within the domain.
 * <p>
 *    This Value Object ensures that any instance of
 *    {@code Email} in the system conforms to standard
 *    format requirements, preventing invalid data from
 *    propagating through the application layers.
 * </p>
 * <p>
 *     For such purposes <b>REGEX</b> patern {@link #EMAIL_PATTERN} is applied.
 * </p>
 *
 * @param value the raw string representation of the email address.
 *              Must comply with {@link #EMAIL_PATTERN}.
 * @since 0.1.0-alpha
 */
public record Email(String value) {
    /**
     * Regular expression pattern used for RFC-compliant
     * email validation.
     * @implNote This pattern supports most modern email
     * structures, including multi-dot domains, while maintaining
     * strictness for common invalid formats.
     */
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+"
                    + "@([a-zA-Z0-9-.]+\\.).[a-zA-Z]+$";

    /**
     * Canonical constructor. Validates the provided string
     * before instantiation.
     *
     * @param value the email address to be validated
     * @throws WrongParametersException if the provided email link is
     * {@code null} or does not match {@link #EMAIL_PATTERN}
     */
    public Email {
        if (!isValid(value)) {
            throw new WrongParametersException(
                    "Invalid email format: " + value
            );
        }
    }

    /**
     * Validates a string against the internal email security and format rules.
     * @param value string to be validated. May be {@code null}.
     * @return {@code true} if the value is non-null and matches the
     * required format; otherwise {@code false}.
     */
    public static boolean isValid(final String value) {
        return value != null && value.matches(EMAIL_PATTERN);
    }

    /**
     * Static factory method for creating of new {@code Email} instance.
     * @param value the raw email string.
     * @return a new validated {@code Email} instance.
     * @throws WrongParametersException if validation fails.
     * @see #Email(String)
     */
    public static Email of(final String value) {
        return new Email(value);
    }
}
