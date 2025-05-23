package org.gs.incode.services.stringtransformation.container.controllers.reports.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = DateIntervalValidator.class)
public @interface DateIntervalValidation {

  String message() default "{constraints.DateIntervalValidation.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
