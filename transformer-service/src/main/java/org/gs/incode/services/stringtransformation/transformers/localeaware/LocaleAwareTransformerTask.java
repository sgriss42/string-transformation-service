package org.gs.incode.services.stringtransformation.transformers.localeaware;

import java.util.Locale;
import java.util.MissingResourceException;
import org.apache.commons.lang3.StringUtils;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;

abstract class LocaleAwareTransformerTask implements TransformerTask {

  protected final Locale locale;

  protected LocaleAwareTransformerTask(String langTag) {
    this.locale = getLocale(langTag);
  }

  protected Locale getLocale(String langTag) {
    if (StringUtils.isBlank(langTag)) {
      return Locale.getDefault();
    }
    try {
      Locale newLocale = Locale.forLanguageTag(langTag);
      validateLocale(newLocale);
      return newLocale;
    } catch (MissingResourceException e) {
      throw new InitTransformationServiceException("Invalid locale format: " + langTag, e);
    }
  }

  /**
   * Validates the given locale, ensuring that it has valid ISO3 language and country codes.
   *
   * @param newLocale the locale to validate; must not be null and must have valid ISO3 language and
   *     country codes
   * @throws MissingResourceException if the locale does not have valid ISO3 language or country
   *     codes
   */
  private void validateLocale(Locale newLocale) {
    newLocale.getISO3Language();
    newLocale.getISO3Country();
  }
}
