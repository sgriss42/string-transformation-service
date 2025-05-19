package org.gs.incode.services.stringtransformation.transformers;

// TODO INTRODUCE LOCALE SUPPORT!!!
public class LowercaseTransformerTask implements TransformerTask {

  @Override
  public String apply(String input) {
    if (input == null) {
      throw new IllegalArgumentException("Input can not be null!");
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
