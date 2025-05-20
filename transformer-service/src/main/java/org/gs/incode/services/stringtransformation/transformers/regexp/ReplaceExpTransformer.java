package org.gs.incode.services.stringtransformation.transformers.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;

public class ReplaceExpTransformer extends RegExpTransformer {
  private final String replacement;

  /**
   * Constructs a transformer that replaces all matches of the given pattern in the input string
   * with the specified replacement string.
   *
   * @param pattern the compiled regular expression pattern used to find matches
   * @param replacement the string to replace each match with
   * @throws IllegalArgumentException if {@code pattern} or {@code replacement} is {@code null}
   */
  protected ReplaceExpTransformer(Pattern pattern, String replacement) {
    super(pattern);
    this.replacement = replacement;
  }

  public static ReplaceExpTransformer of(Pattern pattern, String replacement) {
    if (replacement == null) {
      throw new InitTransformationServiceException("Replacement cannot be null");
    }
    if (pattern == null) {
      throw new InitTransformationServiceException("Pattern cannot be null");
    }
    return new ReplaceExpTransformer(pattern, replacement);
  }

  @Override
  protected String doWithMatched(Matcher matcher) {
    return matcher.replaceAll(replacement);
  }

  @Override
  public String toString() {
    return "[REGEXP REPLACE TASK] pattern %s, replacement %s"
        .formatted(pattern.pattern(), replacement);
  }
}
