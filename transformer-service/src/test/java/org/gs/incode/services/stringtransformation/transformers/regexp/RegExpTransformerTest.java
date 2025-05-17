package org.gs.incode.services.stringtransformation.transformers.regexp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class RegExpTransformerTest {

  @Test
  void whenStringIsNullThanThrowsNPEException() {
    RegExpTransformer transformer = new DummyRegExpTransformer();
    assertThrows(IllegalArgumentException.class, () -> transformer.apply(null));
  }

  @Test
  void whenStringIsBlankThanReturnSameString() {
    RegExpTransformer spyTransformer = Mockito.spy(new DummyRegExpTransformer());
    String emptyString = new String("");
    assertSame(emptyString, spyTransformer.apply(emptyString));
    Mockito.verify(spyTransformer, Mockito.never()).doWithMatched(any(Matcher.class));
  }

  @ParameterizedTest
  @ValueSource(strings = {" ", "  ", " a "})
  void whenStringIsNotEmptyOrNullThanApplyTransform(String input) {
    RegExpTransformer spyTransformer = Mockito.spy(new DummyRegExpTransformer());
    spyTransformer.apply(input);
    Mockito.verify(spyTransformer).doWithMatched(any(Matcher.class));
  }

  static class DummyRegExpTransformer extends RegExpTransformer {

    DummyRegExpTransformer() {
      super(Pattern.compile(""));
    }

    @Override
    protected String doWithMatched(Matcher matcher) {
      return "";
    }
  }
}
