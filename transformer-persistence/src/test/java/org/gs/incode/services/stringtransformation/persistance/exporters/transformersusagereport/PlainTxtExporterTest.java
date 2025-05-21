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
import org.junit.platform.commons.util.StringUtils;

class PlainTxtExporterTest {

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

    PlainTxtExporter exporter = new PlainTxtExporter();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    exporter.export(report, out);

    String result = out.toString(StandardCharsets.UTF_8);
    assertTrue(StringUtils.isNotBlank(result));
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
