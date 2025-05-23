package org.gs.incode.services.stringtransformation.reporting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;
import org.junit.jupiter.api.Test;

class TransformerUsageReportTest {
  @Test
  void addToStatistic() {
    Transformer mockUppercaseTransformer = mockTransformer(TransformerType.TO_UPPERCASE);
    Transformer mockLowercaseTransformer = mockTransformer(TransformerType.TO_LOWERCASE);

    TransformationResultWithTransformers successJob =
        getMockTransformationResult(
            true, List.of(mockUppercaseTransformer, mockUppercaseTransformer));

    TransformationResultWithTransformers failedJob =
        getMockTransformationResult(
            false, List.of(mockUppercaseTransformer, mockLowercaseTransformer));

    ZonedDateTime to = ZonedDateTime.now();
    ZonedDateTime from = to.minusDays(1);

    TransformerUsageReport report = new TransformerUsageReport(from, to);
    report.addToStatistic(List.of(successJob, failedJob));

    assertEquals(to, report.getTo());
    assertEquals(from, report.getFrom());

    assertEquals(2, report.getTotalTransformationCounter());
    assertEquals(1, report.getFailedCounter());
    assertEquals(1, report.getSuccessCounter());
    assertEquals(4L, report.getTotalTransformerCounter());

    Map<TransformerType, Long> stats = report.getStatistic();
    assertEquals(3L, stats.get(TransformerType.TO_UPPERCASE));
    assertEquals(1L, stats.get(TransformerType.TO_LOWERCASE));
  }

  private TransformationResultWithTransformers getMockTransformationResult(
      boolean isSuccess, List<Transformer> transformers) {
    TransformationResultWithTransformers mock = mock(TransformationResultWithTransformers.class);
    when(mock.isJobCompletedSuccessfully()).thenReturn(isSuccess);
    when(mock.getTransformers()).thenReturn(transformers);
    return mock;
  }

  private Transformer mockTransformer(TransformerType type) {
    Transformer mockTransfer = mock(Transformer.class);
    when(mockTransfer.getType()).thenReturn(type);
    return mockTransfer;
  }
}
