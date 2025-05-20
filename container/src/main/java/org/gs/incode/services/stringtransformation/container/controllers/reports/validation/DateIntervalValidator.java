package org.gs.incode.services.stringtransformation.container.controllers.reports.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.gs.incode.services.stringtransformation.container.controllers.reports.TransformationSearchRequest;

public class DateIntervalValidator
    implements ConstraintValidator<DateIntervalValidation, TransformationSearchRequest> {

  @Override
  public boolean isValid(TransformationSearchRequest value, ConstraintValidatorContext context) {
    if (value.getFrom() == null || value.getTo() == null) {
      return true;
    }
    return value.getFrom().isBefore(value.getTo());
  }
}
