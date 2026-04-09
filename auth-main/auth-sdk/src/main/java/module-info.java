module auth.sdk {
    requires auth.kernel;

    exports org.demianov.auth.main.sdk.dto.request;
    exports org.demianov.auth.main.sdk.dto.response;
    exports org.demianov.auth.main.sdk.dto.representation;

    exports org.demianov.auth.main.sdk.exceptions;
}
