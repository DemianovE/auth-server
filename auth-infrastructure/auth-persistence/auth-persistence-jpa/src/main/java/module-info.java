module auth.persistence.jpa {
    requires transitive auth.core;

    requires java.sql;

    requires spring.context;
    requires spring.beans;
    requires spring.orm;
    requires spring.tx;

    requires jakarta.persistence;

    requires static lombok;
    requires org.hibernate.orm.core;
    requires spring.core;
    requires spring.data.jpa;

    exports org.demianov.auth.infrastructure.persistence.jpa.config;

    opens org.demianov.auth.infrastructure.persistence.jpa.config to spring.core, spring.beans, spring.context, org.hibernate.orm.core;
    opens org.demianov.auth.infrastructure.persistence.jpa.entities to spring.core, spring.beans, spring.context, org.hibernate.orm.core;
    opens org.demianov.auth.infrastructure.persistence.jpa.repository to spring.core, spring.beans, spring.context, org.hibernate.orm.core;
    opens org.demianov.auth.infrastructure.persistence.jpa.config.keys to spring.beans, spring.context, spring.core;
    opens org.demianov.auth.infrastructure.persistence.jpa.config.conditions to spring.beans, spring.context;
}
