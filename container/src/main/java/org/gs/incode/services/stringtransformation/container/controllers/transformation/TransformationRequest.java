package org.gs.incode.services.stringtransformation.container.controllers.transformation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransformationRequest {
  @NotBlank
  @Size(max = 10_000)
  private String input;

  @Valid
  @NotNull
  @Size(min = 1, max = 10)
  private List<TransformerConfiguration> transformerConfigurations;

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  static class TransformerConfiguration {

    @NotNull private TransformerType type;

    @Size(max = 100)
    private String regexp;

    @Size(max = 100)
    private String replacement;
  }
}
