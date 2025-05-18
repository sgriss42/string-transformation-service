package org.gs.incode.services.stringtransformation.reporting;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.gs.incode.services.stringtransformation.dtos.TransformationCommand;

@Getter
public class TransformationJobReport {
  private TransformationCommand command;
  private String result;
  private Status status;
  private String errorMessages;
  private UUID id;
  private Boolean isJobCompletedSuccessfully;

  private Instant createdAt;
  private Instant completedAt;

  public void initializeReport(TransformationCommand command) {
    if (this.status != null) {
      throw new IllegalStateException("TransformationJobReport is already initialized");
    }
    this.command = command;
    this.status = Status.NEW;
    this.id = UUID.randomUUID();
    createdAt = Instant.now();
  }

  public void success(String result) {
    if (this.status != Status.NEW) {
      throw new IllegalStateException(
          "TransformationJobReport is not in correct state for setting success result. Current status is %s"
              .formatted(status));
    }
    this.result = result;
    isJobCompletedSuccessfully = true;
    completeReport();
  }

  public void failed(String errorMessages) {
    if (this.status != Status.NEW) {
      throw new IllegalStateException(
          "TransformationJobReport is not in correct state for setting failed result. Current status is %s"
              .formatted(status));
    }
    isJobCompletedSuccessfully = false;
    this.errorMessages = errorMessages;
    completeReport();
  }

  private void completeReport() {
    status = Status.COMPLETED;
    completedAt = Instant.now();
  }
}
