package org.gs.incode.services.stringtransformation.reporting;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransformationResultWithTransformers {

  private final Instant createdAt;
  private final Instant completedAt;
  private final String id;
  private final boolean isJobCompletedSuccessfully;
  private final String errorMessage;
  private final List<Transformer> transformers;

  public TransformationResultWithTransformers(
      Instant createdAt,
      Instant completedAt,
      String id,
      boolean isJobCompletedSuccessfully,
      String errorMessage,
      List<Transformer> transformers) {

    this.createdAt = createdAt;
    this.completedAt = completedAt;
    this.id = id;
    this.isJobCompletedSuccessfully = isJobCompletedSuccessfully;
    this.errorMessage = errorMessage;
    this.transformers = transformers;
  }
}
