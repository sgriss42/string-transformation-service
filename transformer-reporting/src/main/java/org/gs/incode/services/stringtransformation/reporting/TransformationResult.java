package org.gs.incode.services.stringtransformation.reporting;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransformationResult {
  private final Instant createdAt;
  private final Instant completedAt;
  private final String id;
  private final boolean isJobCompletedSuccessfully;
  private final String errorMessage;

  public TransformationResult(
      Instant createdAt,
      Instant completedAt,
      String uuid,
      boolean isJobCompletedSuccessfully,
      String errorMessage) {
    this.createdAt = createdAt;
    this.id = uuid;
    this.isJobCompletedSuccessfully = isJobCompletedSuccessfully;
    this.errorMessage = errorMessage;
    this.completedAt = completedAt;
  }
}
