package org.gs.incode.services.stringtransformation.persistance.exporters.transformersusagereport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;
import org.gs.incode.services.stringtransformation.reporting.TransformationResultWithTransformers;
import org.gs.incode.services.stringtransformation.reporting.Transformer;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;
import org.junit.jupiter.api.Test;

class BaseTransformerUsageReportExporterTest {

  @Test
  void toMap() {
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
    BaseTransformerUsageReportExporter baseTransformerUsageReportExporterTest =
        new PlainTxtExporter();
    Map<String, String> stringStringMap = baseTransformerUsageReportExporterTest.toMap(report);
    assertEquals(10, stringStringMap.size());
    assertEquals(
        from.format(BaseTransformerUsageReportExporter.DEFAULT_DATE_FORMAT),
        stringStringMap.get("FROM"));
    assertEquals(
        to.format(BaseTransformerUsageReportExporter.DEFAULT_DATE_FORMAT),
        stringStringMap.get("TO"));
    assertEquals("2", stringStringMap.get("TOTAL_TRANSFORMATIONS"));
    assertEquals("1", stringStringMap.get("FAILED_TRANSFORMATIONS"));
    assertEquals("1", stringStringMap.get("SUCCESS_TRANSFORMATIONS"));
    assertEquals("4", stringStringMap.get("TOTAL_TRANSFORMERS"));
    assertEquals("3", stringStringMap.get("TO_UPPERCASE"));
    assertEquals("1", stringStringMap.get("TO_LOWERCASE"));
    assertEquals("0", stringStringMap.get("REGEXP_REPLACE"));
    assertEquals("0", stringStringMap.get("REGEXP_DELETE"));
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
