package org.demianov.auth.main.core.exceptions;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "org.demianov.auth.main.core")
public class ExceptionHandlingTest {

    @ArchTest
    public static final ArchRule exceptions_should_extend_base_exception = classes()
            .that().resideInAPackage("..core.exceptions..")
            .and().haveSimpleNameEndingWith("Exception")
            .and().areNotInterfaces()
            .and().doNotHaveSimpleName("AuthCoreException")
            .should().beAssignableTo(AuthCoreException.class)
            .as("Exceptions should extend AuthCoreException");
}
