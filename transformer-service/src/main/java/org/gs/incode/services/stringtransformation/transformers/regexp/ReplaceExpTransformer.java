package org.gs.incode.services.stringtransformation.transformers.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  public ReplaceExpTransformer(Pattern pattern, String replacement) {
    super(pattern);
    if (replacement == null) {
      throw new IllegalArgumentException("Replacement  can not be null");
    }
    this.replacement = replacement;
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
