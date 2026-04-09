package org.demianov.auth.main.core.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

@AnalyzeClasses(
        packages = "org.demianov.auth.main.core",
        importOptions = {ImportOption.DoNotIncludeTests.class}
)
public class CoreArchitectureTest {

    @ArchTest
    public static final ArchRule domain_models_should_be_pure_pojos = classes()
                    .that().resideInAPackage("..domain.models..")
                    .should().onlyDependOnClassesThat()
                    .resideInAnyPackage(
                            "..auth.main.core..",
                            "..auth.main.kernel..",
                            "java..",
                            "org.jetbrains.annotations.."
                    )
                    .as("Domain models must be pure POJOs without framework dependencies");

    @ArchTest
    public static final ArchRule core_should_not_depend_on_infrastructure = noClasses()
                    .that().resideInAPackage("..auth.main.core..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..auth.infrastructure..",
                            "..auth.spring..",
                            "..auth.main.sdk.."
                    )
                    .as("The Core module must not have any knowledge of Infrastructure or Server implementations");

    @ArchTest
    public static final ArchRule services_should_only_depend_on_ports_and_models = classes()
                    .that().resideInAPackage("..application.services..")
                    .should().onlyDependOnClassesThat()
                    .resideInAnyPackage(
                            "..application.ports..",
                            "..application.services.use_case..",
                            "..application.models..",
                            "..domain.models..",
                            "..exceptions..",
                            "org.demianov.auth.main.kernel..",
                            "java..",
                            "org.slf4j..")
            .as("Services should only depend on ports, use cases, models, exceptions, shared and java packages");

    @ArchTest
    static final ArchRule services_should_not_have_spring_annotations = noClasses()
            .that().resideInAPackage("..application.services..")
            .should().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Component")
            .as("Services should not be annotated with Spring annotations");

    @ArchTest static final ArchRule no_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
    @ArchTest static final ArchRule no_generic_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
}
