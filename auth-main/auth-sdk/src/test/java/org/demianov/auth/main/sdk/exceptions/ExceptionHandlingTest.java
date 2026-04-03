package org.demianov.auth.main.sdk.exceptions;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "org.demianov.auth.main.sdk")
public class ExceptionHandlingTest {

    @ArchTest
    public static final ArchRule exceptions_should_implement_tag_exception = classes()
            .that().resideInAPackage("..sdk.exceptions..")
            .and().haveSimpleNameEndingWith("Exception")
            .and().areNotInterfaces()
            .should().beAssignableTo(AuthApiException.class)
            .as("Exceptions should implement AuthApiException");
}
