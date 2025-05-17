package org.gs.incode.services.stringtransformation.job;

import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;

public class TransformationJob {
  @Getter(value = AccessLevel.PACKAGE)
  private final String input;

  private final List<TransformerTask> transformationTasks;

  @Getter() private String result;

  @Getter private String error;
  @Getter private Status status;

  TransformationJob(String input, List<TransformerTask> transformationTasks) {
    if (input == null) {
      throw new IllegalArgumentException("input can not be null!");
    }
    this.input = input;
    this.transformationTasks = transformationTasks;
    status = Status.NEW;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String execute() {
    return switch (status) {
      case COMPLETED -> result;
      case NEW -> applyTransformation();
      case FAILED -> null;
    };
  }

  protected String applyTransformation() {
    if (transformationTasks.isEmpty()) {
      status = Status.COMPLETED;
      result = input;
      return result;
    }
    String resultT = input;

    for (int i = 0; i < transformationTasks.size(); ++i) {
      TransformerTask task = transformationTasks.get(i);
      try {
        resultT = task.apply(resultT);
      } catch (Exception e) {
        status = Status.FAILED;
        error = "Transformation task #%s - %s failed: %s".formatted(i, task, e.getMessage());
        return null;
      }
    }

    status = Status.COMPLETED;
    result = resultT;
    return result;
  }

  public List<TransformerTask> getTransformerTasks() {
    return Collections.unmodifiableList(transformationTasks);
  }

  public boolean isSuccess() {
    return status == Status.COMPLETED;
  }
}
