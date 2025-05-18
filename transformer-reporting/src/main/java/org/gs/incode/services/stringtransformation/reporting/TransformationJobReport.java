package org.gs.incode.services.stringtransformation.reporting;

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
  private boolean isJobCompletedSuccessfully;

  public void initializeReport(TransformationCommand command) {
    if (this.status != null) {
      throw new IllegalStateException("TransformationJobReport is already initialized");
    }
    this.command = command;
    this.status = Status.NEW;
    this.id = UUID.randomUUID();
  }

  public void success(String result) {
    if (this.status != Status.NEW) {
      throw new IllegalStateException(
          "TransformationJobReport is not in correct state for setting success result. Current status is %s"
              .formatted(status));
    }
    status = Status.COMPLETED;
    this.result = result;
    isJobCompletedSuccessfully = true;
  }

  public void failed(String errorMessages) {
    if (this.status != Status.NEW) {
      throw new IllegalStateException(
          "TransformationJobReport is not in correct state for setting failed result. Current status is %s"
              .formatted(status));
    }
    status = Status.COMPLETED;
    this.errorMessages = errorMessages;
    isJobCompletedSuccessfully = false;
  }
}
