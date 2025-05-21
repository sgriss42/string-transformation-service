package org.gs.incode.services.stringtransformation.transformers.localeaware;

import org.gs.incode.services.stringtransformation.exceptions.StringTransformationException;

public class UppercaseTransformerTask extends LocaleAwareTransformerTask {
  public UppercaseTransformerTask() {
    this(null);
  }

  public UppercaseTransformerTask(String locale) {
    super(locale);
  }

  @Override
  public String apply(String input) {
    if (input == null) {
      throw new StringTransformationException("Input can not be null!");
    }
    if (input.isBlank()) {
      return input;
    }

    return input.toUpperCase(locale);
  }

  @Override
  public String toString() {
    return "[TO UPPERCASE TASK]";
  }
}
