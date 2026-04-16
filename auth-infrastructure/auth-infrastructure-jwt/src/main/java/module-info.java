module auth.infrastructure.jwt {
    requires transitive auth.core;
    requires jjwt.api;
    requires static lombok;

    exports org.demianov.auth.infrastructure.jwt;
}
