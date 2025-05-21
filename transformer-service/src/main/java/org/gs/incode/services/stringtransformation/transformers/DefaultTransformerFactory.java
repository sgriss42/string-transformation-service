package org.gs.incode.services.stringtransformation.transformers;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;
import org.gs.incode.services.stringtransformation.transformers.localeaware.LowercaseTransformerTask;
import org.gs.incode.services.stringtransformation.transformers.localeaware.UppercaseTransformerTask;
import org.gs.incode.services.stringtransformation.transformers.regexp.DeleteRegExpTransformer;
import org.gs.incode.services.stringtransformation.transformers.regexp.ReplaceExpTransformer;

public class DefaultTransformerFactory implements TransformerFactory {
  private static final UppercaseTransformerTask uppercaseTransformerTask =
      new UppercaseTransformerTask();
  private static final LowercaseTransformerTask lowercaseTransformerTask =
      new LowercaseTransformerTask();

  @Override
  public TransformerTask construct(TransformerTaskConfig configuration) {
    if (configuration == null) {
      throw new InitTransformationServiceException("configuration can not be null!");
    }
    return switch (configuration.getType()) {
      case TO_UPPERCASE -> getUppercaseTransformerTask(configuration);
      case TO_LOWERCASE -> getLowercaseTransformerTask(configuration);
      case REGEXP_REPLACE ->
          ReplaceExpTransformer.of(
              createPatternFromString(configuration), configuration.replacement());
      case REGEXP_DELETE -> DeleteRegExpTransformer.of(createPatternFromString(configuration));
      default ->
          throw new InitTransformationServiceException(
              "Unsupported transformer type: " + configuration.getType());
    };
  }

  private UppercaseTransformerTask getUppercaseTransformerTask(
      TransformerTaskConfig configuration) {
    if (StringUtils.isBlank(configuration.locale())) {
      return uppercaseTransformerTask;
    }
    return new UppercaseTransformerTask(configuration.locale());
  }

  private LowercaseTransformerTask getLowercaseTransformerTask(
      TransformerTaskConfig configuration) {
    if (StringUtils.isBlank(configuration.locale())) {
      return lowercaseTransformerTask;
    }
    return new LowercaseTransformerTask(configuration.locale());
  }

  private Pattern createPatternFromString(TransformerTaskConfig configuration) {
    if (StringUtils.isBlank(configuration.regexp())) {
      throw new InitTransformationServiceException("Regular expression cannot be null or blank");
    }
    try {
      return Pattern.compile(configuration.regexp());
    } catch (PatternSyntaxException e) {
      throw new InitTransformationServiceException(
          "Invalid regular expression: " + configuration.regexp(), e);
    }
  }
}
