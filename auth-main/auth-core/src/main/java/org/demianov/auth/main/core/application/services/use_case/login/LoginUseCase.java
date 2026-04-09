package org.demianov.auth.main.core.application.services.use_case.login;

import org.demianov.auth.main.core.application.ports.in.login.LoginDispatcher;
import org.demianov.auth.main.kernel.domain.models.Email;

import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.ports.in.login.LoginInputPort;
import org.demianov.auth.main.core.application.ports.out.security.PasswordHasherPort;
import org.demianov.auth.main.core.application.ports.out.persistence.UserRepoPort;
import org.demianov.auth.main.core.application.models.LoginCommand;

import org.demianov.auth.main.core.domain.models.User;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.models.user.UserNotFoundException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

import java.util.Objects;

public final class LoginUseCase implements LoginInputPort {
    /** The user-repository. Used to work with the user persistence layer. */
    private final UserRepoPort userRepo;
    /** The password hasher. Used to hash the user's password. */
    private final PasswordHasherPort passwordHasher;
    /** The login dispatcher. Used to dispatch the login process
     * to the appropriate handler.
     */
    private final LoginDispatcher dispatcher;

    /**
     * Canonical constructor. Perform mandatory field validation.
     * @param userRepoParam user repository.
     * @param passwordHasherParam password hasher.
     * @param dispatcherParam login dispatcher.
     */
    public LoginUseCase(
            final UserRepoPort userRepoParam,
            final PasswordHasherPort passwordHasherParam,
            final LoginDispatcher dispatcherParam) {
        this.userRepo = Objects.requireNonNull(userRepoParam,
                "userRepo cannot be null");
        this.passwordHasher = Objects.requireNonNull(passwordHasherParam,
                "passwordHasher cannot be null");
        this.dispatcher = Objects.requireNonNull(dispatcherParam,
                "dispatcher cannot be null");
    }

    @Override
    public LoginResult execute(final LoginCommand request) {
        try {
            User user = this.userRepo.findByEmail(
                    new Email(request.email().value()))
                    .orElseThrow(() ->
                            new UserNotFoundException(
                                    request.email().value()));
            user.authenticate(request.password(), this.passwordHasher);

            this.userRepo.save(user);

            return this.dispatcher.dispatch(user);
        } catch (AuthCoreException e) {
            if (e instanceof LoginPortExceptions tagged) {
                return new LoginResult.Failure(tagged);
            }
            throw e;
        }
    }
}
