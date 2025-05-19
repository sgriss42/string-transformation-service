package org.gs.incode.services.stringtransformation.reporting;

import java.time.ZonedDateTime;
import java.util.*;
import lombok.Getter;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;

@Getter
public class TransformerUsageReport implements Report {
  private final ZonedDateTime from;
  private final ZonedDateTime to;

  private final Map<TransformerType, Long> statistic;
  private int failedCounter;
  private int successCounter;
  private int totalTransformationCounter;
  private long totalTransformerCounter;

  public TransformerUsageReport(ZonedDateTime from, ZonedDateTime to) {
    if (from == null || to == null) {
      throw new IllegalStateException(
          "From and To  should not be null: from: %s, to: %s".formatted(from, to));
    }
    if (from.isAfter(to)) {
      throw new IllegalStateException(
          "From should be before To.from: %s, to: %s".formatted(from, to));
    }
    this.from = from;
    this.to = to;
    failedCounter = 0;
    successCounter = 0;
    totalTransformationCounter = 0;
    totalTransformerCounter = 0L;
    statistic = new EnumMap<>(TransformerType.class);
    Arrays.stream(TransformerType.values()).forEach(t -> statistic.put(t, 0L));
  }

  public void addToStatistic(List<TransformationResultWithTransformers> transformations) {
    totalTransformationCounter = transformations.size();
    for (TransformationResultWithTransformers transformation : transformations) {
      if (transformation.isJobCompletedSuccessfully()) {
        successCounter++;
      } else {
        failedCounter++;
      }
      totalTransformerCounter += transformation.getTransformers().size();
      for (Transformer transformer : transformation.getTransformers()) {
        statistic.compute(transformer.getType(), (k, oldValue) -> oldValue + 1);
      }
    }
  }

  public Map<String, String> asMap() {
    Map<String, String> result = new LinkedHashMap<>();
    result.put("FROM", from.toString());
    result.put("TO", to.toString());
    result.put("TOTAL_TRANSFORMATIONS", String.valueOf(totalTransformationCounter));
    result.put("FAILED_TRANSFORMATIONS", String.valueOf(failedCounter));
    result.put("SUCCESS_TRANSFORMATIONS", String.valueOf(successCounter));
    result.put("TOTAL_TRANSFORMERS", String.valueOf(totalTransformerCounter));
    statistic.forEach((k, v) -> result.put(k.name(), String.valueOf(v)));
    return result;
  }
}
