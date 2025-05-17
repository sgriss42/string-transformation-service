package org.gs.incode.services.stringtransformation.job;

import java.util.ArrayList;
import java.util.List;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;

public class Builder {
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

  public TransformationJob build() {
    return new TransformationJob(input, transformationTasks);
  }
}
