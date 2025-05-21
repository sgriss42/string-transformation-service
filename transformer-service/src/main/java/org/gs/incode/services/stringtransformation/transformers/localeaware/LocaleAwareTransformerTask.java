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
      newLocale.getISO3Language();
      newLocale.getISO3Country();
      return newLocale;
    } catch (MissingResourceException e) {
      throw new InitTransformationServiceException("Invalid locale format: " + langTag, e);
    }
  }
}
