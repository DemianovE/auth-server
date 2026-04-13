package org.demianov.auth.main.kernel.application.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate internal/default implementation.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Internal {
    /**
     * Default value.
     * @return default value.
     */
    String value() default
            "For internal use only. Subject to change with no notice.";
}
