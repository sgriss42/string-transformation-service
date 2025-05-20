package org.gs.incode.services.stringtransformation.transformers.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;

public abstract class RegExpTransformer implements TransformerTask {
  protected final Pattern pattern;

  /**
   * Constructs a transformer that works with all matches of the given pattern in the input string
   *
   * @param pattern the compiled regular expression pattern used to find matches
   * @throws IllegalArgumentException if {@code pattern} is {@code null}
   */
  RegExpTransformer(Pattern pattern) {
    if (pattern == null) {
      throw new InitTransformationServiceException("Pattern cannot be null");
    }
    this.pattern = pattern;
  }

  /**
   * Applies the transformation to the given input string.
   *
   * @param input the non-null input string to be transformed
   * @return the transformed (modified) string
   * @throws IllegalArgumentException if {@code input} is {@code null}
   */
  @Override
  public String apply(String input) {
    if (null == input) {
      throw new IllegalArgumentException("Input can not be null!");
    }
    if (input.isEmpty()) return input;
    Matcher matcher = pattern.matcher(input);

    return doWithMatched(matcher);
  }

  protected abstract String doWithMatched(Matcher matcher);
}
