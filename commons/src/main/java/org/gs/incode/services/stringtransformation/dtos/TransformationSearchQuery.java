package org.gs.incode.services.stringtransformation.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;

@Data
public class TransformationSearchQuery {

  @NotNull private Instant from;
  @NotNull private Instant to;

  @Min(1)
  private int size;

  @Min(0)
  private int page;
}
