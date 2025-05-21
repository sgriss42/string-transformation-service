package org.gs.incode.services.stringtransformation.transformers.localeaware;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;
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
  void whenStringIsLowerCasedThanReturnSameString(String input) {
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

  @Test
  void whenInvalidLocaleProvidedThenThrowsException() {
    assertThrows(
        InitTransformationServiceException.class,
        () -> new LowercaseTransformerTask("invalid-locale-tag"));
  }

  @Test
  void whenTurkishLocaleUsedThenHandlesSpecialCases() {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask("tr");
    assertEquals("ı", transformer.apply("I"));
    assertEquals("i", transformer.apply("İ"));
  }

  @Test
  void whenGermanLocaleUsedThenHandlesSpecialCases() {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask("de");
    assertEquals("strasse", transformer.apply("STRASSE"));
  }

  @Test
  void whenGreekLocaleUsedThenHandlesSpecialCases() {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask("el");
    assertEquals("ὀδυσσεύς", transformer.apply("ὈΔΥΣΣΕΎΣ"));
  }

  @Test
  void whenLithuanianLocaleUsedThenHandlesSpecialCases() {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask("lt");
    assertEquals("i̇̀", transformer.apply("Ì"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\t", "\n"})
  void whenEmptyLocaleTagProvidedThenUsesDefaultLocale(String emptyLocale) {
    Locale defaultLocale = Locale.getDefault();
    try {
      Locale.setDefault(Locale.US);
      LowercaseTransformerTask transformer = new LowercaseTransformerTask(emptyLocale);
      assertEquals("test", transformer.apply("TEST"));
    } finally {
      Locale.setDefault(defaultLocale);
    }
  }

  @Test
  void whenRtlLocaleUsedThenHandlesBidirectionalText() {
    LowercaseTransformerTask transformer = new LowercaseTransformerTask("ar");
    String input = "مرحبا WORLD";
    String expected = "مرحبا world";
    assertEquals(expected, transformer.apply(input));
  }
}
