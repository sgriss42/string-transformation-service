package org.gs.incode.services.stringtransformation.transformers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.gs.incode.services.stringtransformation.transformers.regexp.DeleteRegExpTransformer;
import org.gs.incode.services.stringtransformation.transformers.regexp.ReplaceExpTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DefaultTransformerFactoryTest {

  @Test
  void whenUppercaseTransformerRequestedThanReturnTransformer() {

    TransformerFactory defaultTransformerFactory = new DefaultTransformerFactory();
    Configuration configuration = new Configuration(TransformerType.TO_UPPERCASE);

    TransformerTask transformerTask = defaultTransformerFactory.construct(configuration);
    assertNotNull(transformerTask);
    assertInstanceOf(UppercaseTransformerTask.class, transformerTask);
    assertSame(
        transformerTask,
        defaultTransformerFactory.construct(new Configuration(TransformerType.TO_UPPERCASE)));
  }

  @Test
  void whenReplaceRegExpTransformerRequestedThanReturnTransformer() {

    TransformerFactory defaultTransformerFactory = new DefaultTransformerFactory();
    Configuration configuration = new Configuration(TransformerType.REGEXP_REPLACE);
    configuration.regexp("regexp");
    configuration.replacement("replacement");

    TransformerTask transformerTask = defaultTransformerFactory.construct(configuration);
    assertNotNull(transformerTask);
    assertInstanceOf(ReplaceExpTransformer.class, transformerTask);
  }

  @Test
  void whenDeleteRegExpTransformerRequestedThanReturnTransformer() {

    TransformerFactory defaultTransformerFactory = new DefaultTransformerFactory();
    Configuration configuration = new Configuration(TransformerType.REGEXP_DELETE);
    configuration.regexp("regexp");

    TransformerTask transformerTask = defaultTransformerFactory.construct(configuration);
    assertNotNull(transformerTask);
    assertInstanceOf(DeleteRegExpTransformer.class, transformerTask);
  }

  @ParameterizedTest
  @MethodSource("invalidConfigurationSource")
  void whenInvalidConfigurationProvidedForReplaceRegExpTransformerThanTExceptionThrows(
      Configuration configuration) {
    TransformerFactory defaultTransformerFactory = new DefaultTransformerFactory();
    assertThrows(
        IllegalArgumentException.class, () -> defaultTransformerFactory.construct(configuration));
  }

  static Stream<Configuration> invalidConfigurationSource() {
    Configuration replaceNoPatternAndReplacement =
        new Configuration(TransformerType.REGEXP_REPLACE);
    Configuration replaceNoPattern = new Configuration(TransformerType.REGEXP_REPLACE);
    replaceNoPattern.regexp("[0-1]");

    Configuration replaceNoReplacement = new Configuration(TransformerType.REGEXP_REPLACE);
    replaceNoReplacement.replacement("someText");

    Configuration deleteNoPatternAndReplacement = new Configuration(TransformerType.REGEXP_DELETE);
    return Stream.of(
        null,
        replaceNoPatternAndReplacement,
        replaceNoPattern,
        replaceNoReplacement,
        deleteNoPatternAndReplacement);
  }
}
