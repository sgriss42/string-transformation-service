package org.gs.incode.services.stringtransformation.job;

import java.util.ArrayList;
import java.util.List;
import org.gs.incode.services.stringtransformation.exceptions.TransformationServiceException;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;

public class Builder {
  public static final int MAX_TRANSFORMER = 10;
  private String input;
  private final List<TransformerTask> transformationTasks;

  public Builder() {
    transformationTasks = new ArrayList<>();
  }

  public Builder input(String input) {
    if (input == null) {
      throw new IllegalArgumentException("input can not be null!");
    }
    this.input = input;
    return this;
  }

  public Builder addTransformerTask(TransformerTask task) {
    if (task == null) {
      throw new IllegalArgumentException("task can not be null!");
    }
    transformationTasks.add(task);
    return this;
  }

  private void validateConfiguration() {
    if (input == null) {
      throw new TransformationServiceException("input can not be null!");
    }
    if (transformationTasks.isEmpty() || transformationTasks.size() > MAX_TRANSFORMER) {
      throw new TransformationServiceException(
          "Too many Transformer Tasks. Allowed: %s, but %s"
              .formatted(MAX_TRANSFORMER, transformationTasks.size()));
    }
    for (int i = 0; i < transformationTasks.size(); ++i) {
      TransformerTask transformerTask = transformationTasks.get(i);
      if (transformerTask == null) {
        throw new TransformationServiceException("Transformer tasks #%s is invalid".formatted(i));
      }
    }
  }

  public TransformationJob build() {
    TransformationJob transformationJob = new TransformationJob(input, transformationTasks);
    validateConfiguration();
    return transformationJob;
  }
}
