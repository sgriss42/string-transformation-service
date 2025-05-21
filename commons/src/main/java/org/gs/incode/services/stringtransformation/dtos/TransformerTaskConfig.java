package org.gs.incode.services.stringtransformation.dtos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.gs.incode.services.stringtransformation.exceptions.TransformationServiceException;

public class TransformerTaskConfig {
  public static final String PARAMS_REGEXP = "regexp";
  public static final String PARAMS_REPLACEMENT = "replacement";
  @Getter private final TransformerType type;
  private final Map<String, String> parameters;

  private TransformerTaskConfig(TransformerType type) {
    this.type = type;
    parameters = new HashMap<>();
  }

  public static TransformerTaskConfig of(TransformerType type) {
    if (type == null) {
      throw new TransformationServiceException("Type of transformer should not be null");
    }
    return new TransformerTaskConfig(type);
  }

  public String regexp() {
    return parameters.get(PARAMS_REGEXP);
  }

  public String replacement() {
    return parameters.get(PARAMS_REPLACEMENT);
  }

  public void regexp(String regexp) {
    parameters.put(PARAMS_REGEXP, regexp);
  }

  public void replacement(String replacement) {
    parameters.put(PARAMS_REPLACEMENT, replacement);
  }

  public Map<String, String> getParameters() {
    return Collections.unmodifiableMap(parameters);
  }
}
