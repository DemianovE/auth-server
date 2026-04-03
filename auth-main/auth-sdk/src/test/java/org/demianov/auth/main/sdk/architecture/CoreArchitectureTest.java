package org.demianov.auth.main.sdk.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

@AnalyzeClasses(
        packages = "org.demianov.auth.main.sdk",
        importOptions = {ImportOption.DoNotIncludeTests.class}
)
public class CoreArchitectureTest {

    @ArchTest
    public static final ArchRule no_classes_from_sdk_should_depend_on_anything_except_kernel = noClasses()
            .that().resideInAnyPackage("..auth.main.sdk..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    "..auth.infrastructure..",
                    "..auth.spring..",
                    "..auth.main.core..")
            .as("SDK objects should not depend on other modules");

    @ArchTest
    public static final ArchRule sdk_classes_should_only_depend_on_kernel_and_pojo = classes()
            .that().resideInAnyPackage("..auth.main.sdk..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    "..auth.main.kernel..",
                    "..auth.main.sdk..",
                    "java..",
                    "org.slf4j..");

    @ArchTest
    public static final ArchRule sdk_should_not_have_spring_annotations = noClasses()
            .that().resideInAPackage("..auth.main.sdk..")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")
            .as("SDK should not be annotated with Spring annotations");

    @ArchTest static final ArchRule no_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
    @ArchTest static final ArchRule no_generic_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
}
