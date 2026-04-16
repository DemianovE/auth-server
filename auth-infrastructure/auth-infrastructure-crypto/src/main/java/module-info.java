module auth.infrastructure.crypto {
    requires transitive auth.core;
    requires spring.security.crypto;

    exports org.demianov.auth.infrastructure.crypto;
}
