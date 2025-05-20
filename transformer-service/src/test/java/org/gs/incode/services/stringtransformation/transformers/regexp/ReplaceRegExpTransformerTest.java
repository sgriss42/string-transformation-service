package org.gs.incode.services.stringtransformation.transformers.regexp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReplaceRegExpTransformerTest {

  @ParameterizedTest
  @MethodSource("sourceForReplace")
  void replace(String input, String regex, String replace, String expected) {
    Pattern pattern = Pattern.compile(regex);

    ReplaceExpTransformer transformer = new ReplaceExpTransformer(pattern, replace);
    assertEquals(expected, transformer.apply(input));
  }

  static Stream<Arguments> sourceForReplace() {
    return Stream.of(
        Arguments.of("abc dfg", "[0-9]", "!!", "abc dfg"),
        Arguments.of("abc1 9dfg", "[0-9]", "!!", "abc!! !!dfg"),
        Arguments.of("abc dfg", "\\w", "", " "),
        Arguments.of("adaccaba", "(c)|(^a)|(a$)", "!", "!da!!ab!"),
        Arguments.of("aaaaa", "a", "a", "aaaaa"),
        Arguments.of("          ", ".+", " ", " "));
  }
}
