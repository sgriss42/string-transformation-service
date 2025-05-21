package org.gs.incode.services.stringtransformation.persistance.exporters.transformersusagereport;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;
import org.gs.incode.services.stringtransformation.reporting.TransformationResultWithTransformers;
import org.gs.incode.services.stringtransformation.reporting.Transformer;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;
import org.junit.jupiter.api.Test;

class CsvExporterTest {

  @Test
  void testExportWritesCsvContent() {
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime to = now.plus(1, ChronoUnit.HOURS);
    TransformerUsageReport report = new TransformerUsageReport(now, to);
    report.addToStatistic(
        List.of(
            createJob(
                true,
                List.of(
                    TransformerType.TO_LOWERCASE,
                    TransformerType.TO_UPPERCASE,
                    TransformerType.REGEXP_DELETE,
                    TransformerType.TO_UPPERCASE,
                    TransformerType.REGEXP_DELETE)),
            createJob(
                false,
                List.of(
                    TransformerType.TO_LOWERCASE,
                    TransformerType.TO_LOWERCASE,
                    TransformerType.TO_UPPERCASE,
                    TransformerType.REGEXP_REPLACE,
                    TransformerType.TO_UPPERCASE))));

    CsvExporter exporter = new CsvExporter();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    exporter.export(report, out);

    String result = out.toString(StandardCharsets.UTF_8);
    assertTrue(result.contains("Field,Value"));
    assertTrue(
        result.contains(
            "FROM,%s"
                .formatted(now.format(BaseTransformerUsageReportExporter.DEFAULT_DATE_FORMAT))));
    assertTrue(
        result.contains(
            "TO,%s".formatted(to.format(BaseTransformerUsageReportExporter.DEFAULT_DATE_FORMAT))));
    assertTrue(result.contains("TOTAL_TRANSFORMATIONS,2"));
    assertTrue(result.contains("FAILED_TRANSFORMATIONS,1"));
    assertTrue(result.contains("SUCCESS_TRANSFORMATIONS,1"));
    assertTrue(result.contains("TOTAL_TRANSFORMERS,10"));
    assertTrue(result.contains("TO_UPPERCASE,4"));
    assertTrue(result.contains("TO_LOWERCASE,3"));
    assertTrue(result.contains("REGEXP_REPLACE,1"));
    assertTrue(result.contains("REGEXP_DELETE,2"));
  }

  TransformationResultWithTransformers createJob(boolean ok, List<TransformerType> types) {
    TransformationResultWithTransformers.TransformationResultWithTransformersBuilder builder =
        TransformationResultWithTransformers.builder();

    List<Transformer> transformers = types.stream().map(i -> new Transformer(i, 1)).toList();

    return builder
        .id(UUID.randomUUID().toString())
        .isJobCompletedSuccessfully(ok)
        .transformers(transformers)
        .build();
  }
}
