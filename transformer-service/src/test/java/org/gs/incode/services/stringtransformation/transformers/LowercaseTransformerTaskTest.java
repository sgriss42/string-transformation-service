package org.gs.incode.services.stringtransformation.transformers;

import static org.junit.jupiter.api.Assertions.*;

import org.gs.incode.services.stringtransformation.exceptions.StringTransformationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LowercaseTransformerTaskTest {

  @Test
  void whenStringIsNullThanThrowsException() {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask();
    assertThrows(StringTransformationException.class, () -> transformer.apply(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "  "})
  void whenStringIsBlankThanReturnSameString(String input) {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask();
    assertSame(input, transformer.apply(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"a", "ab ", "a  b", " a b 1", "123", "#$%", "123a b 1"})
  void whenStringIsUpperCasedThanReturnSameString(String input) {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask();
    assertSame(input, transformer.apply(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"aBc", "a", "aB ", "A  b", " a b 1", "123a", "#A$%", "1b23A B 1"})
  void whenStringIsNotLowerCaseThanReturnUppercase(String input) {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask();
    assertEquals(input.toLowerCase(), transformer.apply(input));
  }

  @Test
  void whenNonLatStringIsNotLowerCaseThanReturnUppercase() {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask();
    String input = "ЙЫЭЖЬЧЯПФЦУЪ ÆØÅ STRASSE ΙΧΘΥΣ ろ ぬ ふ あ う え お や ゆ よ わ ほ ";
    String expected = "йыэжьчяпфцуъ æøå strasse ιχθυς ろ ぬ ふ あ う え お や ゆ よ わ ほ ";
    assertEquals(expected, transformer.apply(input));
  }
}
