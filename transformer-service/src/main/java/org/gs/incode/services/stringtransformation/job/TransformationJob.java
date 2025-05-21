package org.gs.incode.services.stringtransformation.job;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.gs.incode.services.stringtransformation.exceptions.StringTransformationException;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;

public final class TransformationJob {
  public static final int MAX_RESULT_SIZE = 10_000;
  @Getter private final String input;

  private final List<TransformerTask> transformationTasks;

  @Getter() private String result;

  @Getter private String error;
  @Getter private Status status;

  TransformationJob(String input, List<TransformerTask> transformationTasks) {
    this.input = input;
    this.transformationTasks = transformationTasks;
    status = Status.NEW;
  }

  public static Builder builder(String input) {
    Builder builder = new Builder();
    return builder.input(input);
  }

  public String execute() {
    return switch (status) {
      case COMPLETED -> result;
      case NEW -> applyTransformation();
      case FAILED -> null;
    };
  }

  String applyTransformation() {
    if (transformationTasks.isEmpty()) {
      status = Status.COMPLETED;
      result = input;
      return result;
    }
    String tmpResult = input;

    for (int i = 0; i < transformationTasks.size(); ++i) {
      TransformerTask task = transformationTasks.get(i);
      try {
        tmpResult = task.apply(tmpResult);
        if (tmpResult == null || tmpResult.length() > MAX_RESULT_SIZE) {
          Integer actualSize = tmpResult == null ? null : tmpResult.length();
          throw new StringTransformationException(
              "Size of transformed string is bigger than  allowed. Current size %s, allowed %s"
                  .formatted(actualSize, MAX_RESULT_SIZE));
        }
      } catch (Exception e) {
        status = Status.FAILED;
        error = "Transformation task #%s - %s failed: %s".formatted(i, task, e.getMessage());
        throw new StringTransformationException(error, e);
      }
    }

    status = Status.COMPLETED;
    result = tmpResult;
    return result;
  }

  public List<TransformerTask> getTransformerTasks() {
    return Collections.unmodifiableList(transformationTasks);
  }

  public boolean isSuccess() {
    return status == Status.COMPLETED;
  }
}
