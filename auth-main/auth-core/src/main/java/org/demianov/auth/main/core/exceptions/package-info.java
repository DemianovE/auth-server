/**
 * This package contains exceptions for the core layer.
 * <p>
 *     The main bulk of the exceptions are in sub-packages
 *     to avoid cluttering the root package.
 * </p>
 * <p>
 *     All exceptions in the core-module are extensions of
 *     {@link org.demianov.auth.main.core.exceptions.AuthCoreException}
 *     abstract class. This is made for development efficiency
 *     and natural grouping of exceptions.
 * </p>
 * <p>
 *     The exceptions in core-module also have {@code tags}.
 *     These tags are used to group exceptions together based
 *     on their expected usage in use-cases logic-flows. These tags
 *     do not replace the {@code AuthCoreException} but serve
 *     the purpose of better grouping based on logic.
 * </p>
 * @since 0.1.0-alpha
 */
package org.demianov.auth.main.core.exceptions;
