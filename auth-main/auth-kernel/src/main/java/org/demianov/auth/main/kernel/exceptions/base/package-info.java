/**
 * Base system exceptions.
 * <p>
 *     This package contains the base exception classes for the application,
 *     which are used to handle mapping to API-friendly responses.
 * </p>
 *
 * <p>
 *     The
 *     {@link org.demianov.auth.main.kernel.exceptions.base.PlatformException}
 *     serves as the root for all domain and infrastructure exceptions, and
 *     logic base to how the data is stored to be correctly mapped to
 *     API response.
 * </p>
 *
 * <p>
 *     All other exceptions in this package are derived from
 *     {@code PlatformException} and represent the <i>base cases</i>
 *     which can be triggered.
 * </p>
 * @since 0.1.0-alpha
 */
package org.demianov.auth.main.kernel.exceptions.base;
