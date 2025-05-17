package org.gs.incode.services.stringtransformation.transformers.regexp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DeleteRegExpTransformerTest {

  @ParameterizedTest
  @MethodSource("sourceForDelete")
  void delete(String input, String regex, String expected) {
    Pattern pattern = Pattern.compile(regex);
    DeleteRegExpTransformer transformer = new DeleteRegExpTransformer(pattern);
    assertEquals(expected, transformer.apply(input));
  }

  static Stream<Arguments> sourceForDelete() {
    return Stream.of(
        Arguments.of("          ", "\\s", ""),
        Arguments.of(" 1 2 3 ", "\\s", "123"),
        Arguments.of(" 1 2 3 ", "[0-9]", "    "),
        Arguments.of(" 1 2 3 ", "[a-z]", " 1 2 3 "),
        Arguments.of(" 1 2 3 ", ".+", ""));
  }
}
