package org.gs.incode.services.stringtransformation.reporting;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransformationResult {
  private final Instant timestamp;
  private final String id;
  private final boolean isSuccessful;
  private final String error;

  public TransformationResult(Instant timestamp, String uuid, boolean isSuccessful, String error) {
    this.timestamp = timestamp;
    this.id = uuid;
    this.isSuccessful = isSuccessful;
    this.error = error;
  }
}
