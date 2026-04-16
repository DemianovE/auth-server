module auth.core {
    requires transitive auth.kernel;

    exports org.demianov.auth.main.core.domain.models;
    exports org.demianov.auth.main.core.application.models;

    exports org.demianov.auth.main.core.application.ports.in.login;
    exports org.demianov.auth.main.core.application.ports.out.security;
    exports org.demianov.auth.main.core.application.ports.out.persistence;

    exports org.demianov.auth.main.core.exceptions;

    opens org.demianov.auth.main.core.application.services.domain_services to spring.core, spring.beans, spring.context;
    opens org.demianov.auth.main.core.application.services.use_case.login to spring.core, spring.beans, spring.context;
    exports org.demianov.auth.main.core.application.ports.out.listeners;
    exports org.demianov.auth.main.core.exceptions.models.token;
    exports org.demianov.auth.main.core.application.ports.out.common;
    exports org.demianov.auth.main.core.exceptions.ports;
}
