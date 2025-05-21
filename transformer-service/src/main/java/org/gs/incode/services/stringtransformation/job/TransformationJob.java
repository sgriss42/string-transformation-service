package org.gs.incode.services.stringtransformation.job;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.gs.incode.services.stringtransformation.exceptions.StringTransformationException;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;

public class TransformationJob {
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

  public static TransformationJobBuilder builder(String input) {
    TransformationJobBuilder transformationJobBuilder = new TransformationJobBuilder();
    return transformationJobBuilder.input(input);
  }

  /**
   * Executes the transformation job based on the current status. If the status is {@code
   * COMPLETED}, returns the previously computed result. If the status is {@code NEW}, initiates and
   * applies the transformation through a series of tasks. If the status is {@code FAILED}, throws a
   * {@code StringTransformationException} with the associated error message.
   *
   * @return The transformed string if the job completes successfully or the previously computed
   *     result.
   * @throws StringTransformationException If the status is {@code FAILED} or if a transformation
   *     task fails.
   */
  public String execute() {
    return switch (status) {
      case COMPLETED -> result;
      case NEW -> applyTransformation();
      case FAILED -> throw new StringTransformationException(error);
    };
  }

  protected String applyTransformation() {
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
