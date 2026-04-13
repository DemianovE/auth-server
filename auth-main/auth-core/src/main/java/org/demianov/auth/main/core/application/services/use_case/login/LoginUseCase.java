package org.demianov.auth.main.core.application.services.use_case.login;

import org.demianov.auth.main.core.application.models.FailureStrategy;
import org.demianov.auth.main.core.application.models.TokenPair;
import org.demianov.auth.main.core.application.ports.in.login.CriticalLoginHandler;
import org.demianov.auth.main.core.application.ports.in.login.LoginDispatcher;
import org.demianov.auth.main.core.application.ports.out.listeners.LoginListener;
import org.demianov.auth.main.core.application.ports.out.security.TokenGeneratorPort;
import org.demianov.auth.main.core.exceptions.ports.UnexpectedSystemException;
import org.demianov.auth.main.kernel.api.Prioritized;

import org.demianov.auth.main.core.application.models.LoginResult;
import org.demianov.auth.main.core.application.ports.in.login.LoginInputPort;
import org.demianov.auth.main.core.application.ports.out.security.PasswordHasherPort;
import org.demianov.auth.main.core.application.ports.out.persistence.UserRepoPort;
import org.demianov.auth.main.core.application.models.LoginCommand;

import org.demianov.auth.main.core.domain.models.User;

import org.demianov.auth.main.core.exceptions.AuthCoreException;
import org.demianov.auth.main.core.exceptions.models.user.UserNotFoundException;
import org.demianov.auth.main.core.exceptions.tags.LoginPortExceptions;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public final class LoginUseCase implements LoginInputPort {
    /** The user-repository. Used to work with the user persistence layer. */
    private final UserRepoPort userRepo;
    /** The password hasher. Used to hash the user's password. */
    private final PasswordHasherPort passwordHasher;
    /** The login dispatcher. Used to dispatch the login process
     * to the appropriate handler.*/
    private final LoginDispatcher dispatcher;
    /** The token generator. Used to generate the user's tokens. */
    private final TokenGeneratorPort tokenGenerator;
    /** The critical handlers. Used to perform critical actions */
    private final List<CriticalLoginHandler> criticalHandlers;
    /** The login listeners. Used to perform actions after the login process. */
    private final List<LoginListener> listeners;
    /** The failure strategy. By default, is
     * {@link FailureStrategy#CONTINUE_ON_FAIL} */
    private final FailureStrategy failureStrategy;

    LoginUseCase(final UserRepoPort userRepoParam,
                 final PasswordHasherPort passwordHasherParam,
                 final LoginDispatcher dispatcherParam,
                 final TokenGeneratorPort tokenGeneratorParam,
                 final List<CriticalLoginHandler> criticalHandlersParam,
                 final List<LoginListener> listenersParam,
                 final FailureStrategy failureStrategyParam) {
        this.userRepo = userRepoParam;
        this.passwordHasher = passwordHasherParam;
        this.dispatcher = dispatcherParam;
        this.tokenGenerator = tokenGeneratorParam;
        this.criticalHandlers = sortByPriority(criticalHandlersParam);
        this.listeners = sortByPriority(listenersParam);
        this.failureStrategy = failureStrategyParam;
    }

    @Override
    public LoginResult execute(final LoginCommand request) {
        try {
            User user = this.userRepo.findByEmail(request.email())
                    .orElseThrow(() ->
                            new UserNotFoundException(
                                    request.email().value()));

            executeHandlers(this.criticalHandlers,
                    handler -> handler.perform(user));

            user.authenticate(request.password(), this.passwordHasher);

            this.userRepo.save(user);

            TokenPair generatedPair = this.tokenGenerator.generate(user);
            LoginResult result = new LoginResult.Success(
                    generatedPair.accessToken(),
                    generatedPair.refreshToken(),
                    generatedPair.expiresIn(),
                    generatedPair.refreshTokenTtl()
            );

            executeHandlers(this.listeners,
                    listener -> listener.onSuccess(user));
            return this.dispatcher.dispatch(result, user);
        } catch (AuthCoreException e) {
            return handleFailure(e, request);
        }
    }

    /**
     * Perform the failure logic.
     * @param e the exception.
     * @param request the login command request payload.
     * @return the login result.
     * @implNote The failure listeners if failed will be ignored. As such
     * the original exception will be <b>always</b> thrown
     */
    private LoginResult handleFailure(
            final AuthCoreException e,
            final LoginCommand request) {
        try {
            executeHandlers(this.listeners,
                    listener -> listener.onFailure(request));
        } catch (Exception ignored) {
        }

        return switch (e) {
            case LoginPortExceptions tagged -> new LoginResult.Failure(tagged);

            default -> new LoginResult.Failure(
                    new UnexpectedSystemException(e));
        };
    }

    /**
     * Perform handler logic. If the failure strategy is FAIL_FAST,
     * then the exception is thrown.
     * @param handlers list of handlers.
     * @param action action to perform.
     * @param <T> type of the handlers.
     */
    private <T> void executeHandlers(
            final List<T> handlers,
            final Consumer<T> action) {
        for (T handler : handlers) {
            try {
                action.accept(handler);
            } catch (AuthCoreException e) {
                if (this.failureStrategy == FailureStrategy.FAIL_FAST
                        || handler instanceof CriticalLoginHandler) {
                    throw e;
                }
            } catch (Exception e) {
                if (this.failureStrategy == FailureStrategy.FAIL_FAST
                        || handler instanceof CriticalLoginHandler) {
                    throw new UnexpectedSystemException(e);
                }
            }
        }
    }

    /**
     * Perform Priotiy sorting on a list of handlers.
     * @param listOfValues list of handlers.
     * @return sorted the list of handlers.
     * @param <T> type of the handlers.
     */
    private <T extends Prioritized> List<T> sortByPriority(
            final List<T> listOfValues) {
        if (listOfValues == null) {
            return List.of();
        }

        return listOfValues.stream()
                .sorted(Comparator.comparingInt(
                        Prioritized::getPriority).reversed())
                .toList();
    }
}
