package org.gs.incode.services.stringtransformation.transformers;

import static org.junit.jupiter.api.Assertions.*;

import org.gs.incode.services.stringtransformation.exceptions.StringTransformationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UppercaseTransformerTaskTest {

  @Test
  void whenStringIsNullThanThrowsException() {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask();
    assertThrows(StringTransformationException.class, () -> transformer.apply(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "  "})
  void whenStringIsBlankThanReturnSameString(String input) {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask();
    assertSame(input, transformer.apply(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"A", "AB ", "A  B", " A B 1", "123", "#$%", "123A B 1"})
  void whenStringIsUpperCasedThanReturnSameString(String input) {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask();
    assertSame(input, transformer.apply(input));
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc", "a", "aB ", "A  b", " a b 1", "123a", "#A$%", "1b23A B 1"})
  void whenStringIsNotUpperCasedThanReturnUppercase(String input) {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask();
    assertEquals(input.toUpperCase(), transformer.apply(input));
  }

  @Test
  void whenNonLatStringIsNotUpperCasedThanReturnUppercase() {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask();
    String input = "йыэжьчяпфцуъ æøå Straße ろ ぬ ふ あ う え お や ゆ よ わ ほ ";
    String expected = "ЙЫЭЖЬЧЯПФЦУЪ ÆØÅ STRASSE ろ ぬ ふ あ う え お や ゆ よ わ ほ ";
    assertEquals(expected, transformer.apply(input));
  }
}
