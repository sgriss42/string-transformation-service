package org.gs.incode.services.stringtransformation.transformers;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class Configuration {
  @Getter private final TransformerType type;
  private final Map<String, String> parameters;

  public Configuration(TransformerType type) {
    this.type = type;
    parameters = new HashMap<>();
  }

  String regexp() {
    return parameters.get("regexp");
  }

  String replacement() {
    return parameters.get("replacement");
  }

  public void regexp(String regexp) {
    parameters.put("regexp", regexp);
  }

  public void replacement(String replacement) {
    parameters.put("replacement", replacement);
  }
}
