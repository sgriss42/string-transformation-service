package org.gs.incode.services.stringtransformation.transformers.regexp;

import java.util.regex.Pattern;

public class DeleteRegExpTransformer extends ReplaceExpTransformer {
  /**
   * Constructs a transformer that remove all matches of the given pattern in the input string.
   *
   * @param pattern the compiled regular expression pattern used to find matches
   * @throws IllegalArgumentException if {@code pattern} is {@code null}
   */
  public DeleteRegExpTransformer(Pattern pattern) {
    super(pattern, "");
  }

  @Override
  public String toString() {
    return "[REGEXP DELETE TASK] pattern %s".formatted(pattern.pattern());
  }
}
