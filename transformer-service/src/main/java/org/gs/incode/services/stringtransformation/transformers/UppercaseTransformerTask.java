package org.gs.incode.services.stringtransformation.transformers;

public class UppercaseTransformerTask implements TransformerTask {

  @Override
  public String apply(String input) {
    if (input == null) {
      throw new NullPointerException();
    }
    if (input.isBlank()) {
      return input;
    }

    return input.toUpperCase();
  }
}
