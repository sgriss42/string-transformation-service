package org.gs.incode.services.stringtransformation.reporting;

import lombok.Builder;
import lombok.Getter;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;

@Builder
@Getter
public class Transformer {
  private final TransformerType type;
  private final Integer id;

  public Transformer(TransformerType type, Integer id) {
    this.type = type;
    this.id = id;
  }
}
