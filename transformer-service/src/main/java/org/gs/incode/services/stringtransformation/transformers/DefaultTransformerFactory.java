package org.gs.incode.services.stringtransformation.transformers;

import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.gs.incode.services.stringtransformation.transformers.regexp.DeleteRegExpTransformer;
import org.gs.incode.services.stringtransformation.transformers.regexp.ReplaceExpTransformer;

public class DefaultTransformerFactory implements TransformerFactory {
  private final UppercaseTransformerTask uppercaseTransformerTask;

  public DefaultTransformerFactory() {
    uppercaseTransformerTask = new UppercaseTransformerTask();
  }

  @Override
  public TransformerTask construct(Configuration configuration) {
    if (configuration == null) {
      throw new IllegalArgumentException("configuration can not be null!");
    }
    return switch (configuration.getType()) {
      case TO_UPPERCASE -> uppercaseTransformerTask;
      case REGEXP_REPLACE ->
          new ReplaceExpTransformer(
              createPatternFromString(configuration), configuration.replacement());
      case REGEXP_DELETE -> new DeleteRegExpTransformer(createPatternFromString(configuration));
    };
  }

  protected static Pattern createPatternFromString(Configuration configuration) {
    if (StringUtils.isBlank(configuration.regexp())) {
      throw new IllegalArgumentException("Regular expression cannot be null or blank");
    }
    return Pattern.compile(configuration.regexp());
  }
}
