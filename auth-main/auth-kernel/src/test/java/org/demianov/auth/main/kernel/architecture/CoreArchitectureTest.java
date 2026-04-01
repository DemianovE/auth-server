package org.demianov.auth.main.kernel.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

@AnalyzeClasses(
        packages = "org.demianov.auth.main.kernel",
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class CoreArchitectureTest {

    @ArchTest
    public static final ArchRule kernel_should_not_depend_on_any_module = noClasses()
            .that().resideInAPackage("..main.kernel..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    "..auth.infrastructure..",
                    "..auth.spring..",
                    "..auth.main.core..",
                    "..auth.main.sdk..")
            .as("Shared kernel should not depend on any other module");

    @ArchTest
    public static final ArchRule kernel_should_be_pure_pojo = classes()
            .that().resideInAPackage("..main.kernel..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    "..auth.main.kernel..",
                    "java..",
                    "org.slf4j..")
            .as("Shared kernel should be pure POJO");

    @ArchTest
    public static final ArchRule kernel_should_not_have_spring_annotations = noClasses()
            .that().resideInAPackage("..main.kernel..")
            .should().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Component")
            .as("Kernel should not be annotated with Spring annotations");

    @ArchTest static final ArchRule no_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
    @ArchTest static final ArchRule no_generic_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
}
