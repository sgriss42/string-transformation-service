package org.gs.incode.services.stringtransformation.transformers;

import org.gs.incode.services.stringtransformation.exceptions.StringTransformationException;

// TODO INTRODUCE LOCALE SUPPORT!!!
public class LowercaseTransformerTask implements TransformerTask {

  @Override
  public String apply(String input) {
    if (input == null) {
      throw new StringTransformationException("Input can not be null!");
    }
    if (input.isBlank()) {
      return input;
    }

    return input.toLowerCase();
  }

  @Override
  public String toString() {
    return "[TO LOWERCASE TASK]";
  }
}
