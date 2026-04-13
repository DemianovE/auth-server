package org.demianov.auth.main.core.application.ports.in.login;

import org.demianov.auth.main.core.application.models.LoginCommand;
import org.demianov.auth.main.core.application.models.LoginResult;

import org.demianov.auth.main.core.exceptions.models.token.TokenException;
import org.demianov.auth.main.core.exceptions.models.token.TokenInvalidException;
import org.demianov.auth.main.core.exceptions.models.user.PasswordMismatchException;
import org.demianov.auth.main.core.exceptions.models.user.UserAccountException;
import org.demianov.auth.main.core.exceptions.models.user.UserNotFoundException;
import org.demianov.auth.main.core.exceptions.DataAccessException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

/**
 * The inwards port for a Login process.
 * <p>
 *     Port represents the boundary API for the login process.
 *     Abstracting the implementation details of the login process.
 * </p>
 *
 * <p>
 *     The port uses {@link LoginResult} as the reurn, as such
 *     the controler should handle the mapping for the following possible
 *     exceptions:
 *     <ul>
 *         <li>{@link DataAccessException} - the persistence repository
 *              failure, system reasons;</li>
 *         <li>{@link UserNotFoundException} - the user was not found;</li>
 *         <li>{@link PasswordMismatchException} - the password string is not
 *              mattched to the hashed password;</li>
 *         <li>{@link UserAccountException} - the user accounts'
 *              lifecycle status doesn't allow for such action<;/li>
 *         <li>{@link TokenException} - the token creation process
 *              failed;</li>
 *         <li>{@link TokenInvalidException} - the token validation
 *              failed;</li>
 *     </ul>
 * </p>
 * <p>
 *     Additionaly, a use-case can have {@link CriticalLoginHandler}.
 *     If any of them throws the exceptions, the flow will be stopped
 *     and the {@link LoginResult.Failure} will be returned.
 * </p>
 *
 * @since 0.1.0-alpha
 */
public interface LoginInputPort {

    /**
     * Executes the use-case process.
     * <p>
     *     As a default input function, the method should
     *     perform the login process and return the {@link LoginResult}
     *     with repoctive {@code Successfull} or {@code Failed} state.
     * </p>
     *
     * <p>
     *     The use case is not throwing exceptions which are part of
     *     {@link LoginPortExceptions} tag group, as they are handled
     *     using the {@link LoginResult.Failure}. Any other exception
     *     is out of the scope of expected and as such will be thrown.
     * </p>
     *
     * @param request login request DTO.
     * @return generated tokens in DTO.
     */
    LoginResult execute(LoginCommand request);
}
