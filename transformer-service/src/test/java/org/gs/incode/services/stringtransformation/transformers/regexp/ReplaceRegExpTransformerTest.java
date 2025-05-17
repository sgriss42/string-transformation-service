package org.gs.incode.services.stringtransformation.transformers.regexp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
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

  @Test
  void maxInputReplace() {
    String input = RandomStringUtils.secure().nextAlphabetic(10_000);
    Pattern pattern = Pattern.compile("\\w");
    long start = System.currentTimeMillis();
    ReplaceExpTransformer transformer = new ReplaceExpTransformer(pattern, "1");
    String result = transformer.apply(input);
    long end = System.currentTimeMillis();
    System.out.println("transform takes: " + (end - start));
    assertEquals(10_000, result.length());
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
