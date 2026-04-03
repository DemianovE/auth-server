/**
 * Auth Kernel is the core of the application, the "Source of Truth".
 */
module auth.kernel {
    exports org.demianov.auth.main.kernel.domain.models;

    exports org.demianov.auth.main.kernel.application.ports.in;
    exports org.demianov.auth.main.kernel.application.models;

    exports org.demianov.auth.main.kernel.exceptions.base to auth.core, auth.sdk;
}
