package org.demianov.auth.main.kernel.exceptions;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.demianov.auth.main.kernel.exceptions.base.PlatformException;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "org.demianov.auth.main.kernel")
public class ExceptionHandlingTest {

    @ArchTest
    public static final ArchRule exceptions_should_extend_base_exception = classes()
            .that().resideInAPackage("..kernel.exceptions..")
            .and().haveSimpleNameEndingWith("Exception")
            .and().areNotInterfaces()
            .and().doNotHaveSimpleName("PlatformException")
            .should().beAssignableTo(PlatformException.class)
            .as("Exceptions should extend PlatformException");
}
