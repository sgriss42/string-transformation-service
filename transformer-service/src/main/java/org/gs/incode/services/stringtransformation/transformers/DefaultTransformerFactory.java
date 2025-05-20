package org.gs.incode.services.stringtransformation.transformers;

import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;
import org.gs.incode.services.stringtransformation.exceptions.TransformationServiceException;
import org.gs.incode.services.stringtransformation.transformers.regexp.DeleteRegExpTransformer;
import org.gs.incode.services.stringtransformation.transformers.regexp.ReplaceExpTransformer;

public class DefaultTransformerFactory implements TransformerFactory {
  private final UppercaseTransformerTask uppercaseTransformerTask;
  private final LowercaseTransformerTask lowercaseTransformerTask;

  public DefaultTransformerFactory() {
    uppercaseTransformerTask = new UppercaseTransformerTask();
    lowercaseTransformerTask = new LowercaseTransformerTask();
  }

  @Override
  public TransformerTask construct(TransformerTaskConfig configuration) {
    if (configuration == null) {
      throw new IllegalArgumentException("configuration can not be null!");
    }
    return switch (configuration.getType()) {
      case TO_UPPERCASE -> uppercaseTransformerTask;
      case TO_LOWERCASE -> lowercaseTransformerTask;
      case REGEXP_REPLACE ->
          new ReplaceExpTransformer(
              createPatternFromString(configuration), configuration.replacement());
      case REGEXP_DELETE -> new DeleteRegExpTransformer(createPatternFromString(configuration));
    };
  }

  protected static Pattern createPatternFromString(TransformerTaskConfig configuration) {
    if (StringUtils.isBlank(configuration.regexp())) {
      throw new TransformationServiceException("Regular expression cannot be null or blank");
    }

    return Pattern.compile(configuration.regexp());
  }
}
