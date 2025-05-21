package org.gs.incode.services.stringtransformation.transformers.localeaware;

import org.gs.incode.services.stringtransformation.exceptions.StringTransformationException;

public class LowercaseTransformerTask extends LocaleAwareTransformerTask {
  public LowercaseTransformerTask() {
    this(null);
  }

  public LowercaseTransformerTask(String locale) {
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

    return input.toLowerCase(locale);
  }

  @Override
  public String toString() {
    return "[TO LOWERCASE TASK]";
  }
}
