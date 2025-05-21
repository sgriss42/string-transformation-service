package org.gs.incode.services.stringtransformation.transformers.localeaware;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;
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

  @Test
  void whenInvalidLocaleProvidedThenThrowsException() {
    assertThrows(
        InitTransformationServiceException.class,
        () -> new UppercaseTransformerTask("invalid-locale-tag"));
  }

  @Test
  void whenTurkishLocaleUsedThenHandlesSpecialCases() {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask("tr");
    assertEquals("I", transformer.apply("ı"));
    assertEquals("İ", transformer.apply("i"));
  }

  @Test
  void whenGermanLocaleUsedThenHandlesSpecialCases() {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask("de");
    assertEquals("STRASSE", transformer.apply("straße"));
  }

  @Test
  void whenGreekLocaleUsedThenHandlesSpecialCases() {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask("el");
    assertEquals("ὈΔΥΣΣΕΎΣ", transformer.apply("ὀδυσσεύς"));
  }

  @Test
  void whenLithuanianLocaleUsedThenHandlesSpecialCases() {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask("lt");
    assertEquals("Ì", transformer.apply("ì"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\t", "\n"})
  void whenEmptyLocaleTagProvidedThenUsesDefaultLocale(String emptyLocale) {
    Locale defaultLocale = Locale.getDefault();
    try {
      Locale.setDefault(Locale.US);
      UppercaseTransformerTask transformer = new UppercaseTransformerTask(emptyLocale);
      assertEquals("TEST", transformer.apply("test"));
    } finally {
      Locale.setDefault(defaultLocale);
    }
  }

  @Test
  void whenRtlLocaleUsedThenHandlesBidirectionalText() {
    UppercaseTransformerTask transformer = new UppercaseTransformerTask("ar");
    String input = "مرحبا world";
    String expected = "مرحبا WORLD";
    assertEquals(expected, transformer.apply(input));
  }
}
