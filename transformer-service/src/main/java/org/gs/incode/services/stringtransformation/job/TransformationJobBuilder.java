package org.gs.incode.services.stringtransformation.job;

import java.util.ArrayList;
import java.util.List;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;

public class TransformationJobBuilder {
  public static final int MAX_TRANSFORMER = 10;
  private String input;
  private final List<TransformerTask> transformationTasks;

  public TransformationJobBuilder() {
    transformationTasks = new ArrayList<>();
  }

  public TransformationJobBuilder input(String input) {
    if (input == null) {
      throw new InitTransformationServiceException("input can not be null!");
    }
    this.input = input;
    return this;
  }

  public TransformationJobBuilder addTransformerTask(TransformerTask task) {
    if (task == null) {
      throw new InitTransformationServiceException("task can not be null!");
    }
    transformationTasks.add(task);
    return this;
  }

  private void validateConfiguration() {
    if (input == null) {
      throw new InitTransformationServiceException("input can not be null!");
    }
    if (input.length() > TransformationJob.MAX_RESULT_SIZE) {
      throw new InitTransformationServiceException(
          "Input is too long. Max size: %s".formatted(TransformationJob.MAX_RESULT_SIZE));
    }
    if (transformationTasks.isEmpty()) {
      throw new InitTransformationServiceException("At least one transformer task is required");
    }

    if (transformationTasks.size() > MAX_TRANSFORMER) {
      throw new InitTransformationServiceException(
          "Too many Transformer Tasks. Allowed: %s, found: %s"
              .formatted(MAX_TRANSFORMER, transformationTasks.size()));
    }
  }

  public TransformationJob build() {
    validateConfiguration();
    return new TransformationJob(input, transformationTasks);
  }
}
