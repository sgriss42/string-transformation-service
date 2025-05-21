package org.gs.incode.services.stringtransformation.transformers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;
import org.gs.incode.services.stringtransformation.transformers.localeaware.LowercaseTransformerTask;
import org.gs.incode.services.stringtransformation.transformers.localeaware.UppercaseTransformerTask;
import org.gs.incode.services.stringtransformation.transformers.regexp.DeleteRegExpTransformer;
import org.gs.incode.services.stringtransformation.transformers.regexp.ReplaceExpTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DefaultTransformerFactoryTest {

  @Test
  void whenUppercaseTransformerRequestedThanReturnTransformer() {

    TransformerFactory defaultTransformerFactory = new DefaultTransformerFactory();
    TransformerTaskConfig configuration = TransformerTaskConfig.of(TransformerType.TO_UPPERCASE);

    TransformerTask transformerTask = defaultTransformerFactory.construct(configuration);
    assertNotNull(transformerTask);
    assertInstanceOf(UppercaseTransformerTask.class, transformerTask);
    assertSame(
        transformerTask,
        defaultTransformerFactory.construct(
            TransformerTaskConfig.of(TransformerType.TO_UPPERCASE)));
  }

  @Test
  void whenLowercaseTransformerRequestedThanReturnTransformer() {

    TransformerFactory defaultTransformerFactory = new DefaultTransformerFactory();
    TransformerTaskConfig configuration = TransformerTaskConfig.of(TransformerType.TO_LOWERCASE);

    TransformerTask transformerTask = defaultTransformerFactory.construct(configuration);
    assertNotNull(transformerTask);
    assertInstanceOf(LowercaseTransformerTask.class, transformerTask);
    assertSame(
        transformerTask,
        defaultTransformerFactory.construct(
            TransformerTaskConfig.of(TransformerType.TO_LOWERCASE)));
  }

  @Test
  void whenReplaceRegExpTransformerRequestedThanReturnTransformer() {

    TransformerFactory defaultTransformerFactory = new DefaultTransformerFactory();
    TransformerTaskConfig configuration = TransformerTaskConfig.of(TransformerType.REGEXP_REPLACE);
    configuration.regexp("regexp");
    configuration.replacement("replacement");

    TransformerTask transformerTask = defaultTransformerFactory.construct(configuration);
    assertNotNull(transformerTask);
    assertInstanceOf(ReplaceExpTransformer.class, transformerTask);
  }

  @Test
  void whenDeleteRegExpTransformerRequestedThanReturnTransformer() {

    TransformerFactory defaultTransformerFactory = new DefaultTransformerFactory();
    TransformerTaskConfig configuration = TransformerTaskConfig.of(TransformerType.REGEXP_DELETE);
    configuration.regexp("regexp");

    TransformerTask transformerTask = defaultTransformerFactory.construct(configuration);
    assertNotNull(transformerTask);
    assertInstanceOf(DeleteRegExpTransformer.class, transformerTask);
  }

  @ParameterizedTest
  @MethodSource("invalidConfigurationSource")
  void whenInvalidConfigurationProvidedForReplaceRegExpTransformerThanExceptionThrows(
      TransformerTaskConfig configuration) {
    TransformerFactory defaultTransformerFactory = new DefaultTransformerFactory();
    assertThrows(
        InitTransformationServiceException.class,
        () -> defaultTransformerFactory.construct(configuration));
  }

  static Stream<TransformerTaskConfig> invalidConfigurationSource() {
    TransformerTaskConfig replaceNoPatternAndReplacement =
        TransformerTaskConfig.of(TransformerType.REGEXP_REPLACE);
    TransformerTaskConfig replaceNoPattern =
        TransformerTaskConfig.of(TransformerType.REGEXP_REPLACE);
    replaceNoPattern.regexp("[0-1]");

    TransformerTaskConfig replaceNoReplacement =
        TransformerTaskConfig.of(TransformerType.REGEXP_REPLACE);
    replaceNoReplacement.replacement("someText");

    TransformerTaskConfig deleteNoPatternAndReplacement =
        TransformerTaskConfig.of(TransformerType.REGEXP_DELETE);
    return Stream.of(
        null,
        replaceNoPatternAndReplacement,
        replaceNoPattern,
        replaceNoReplacement,
        deleteNoPatternAndReplacement);
  }
}
