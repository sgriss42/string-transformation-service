package org.gs.incode.services.stringtransformation.persistance.exporters.transformersusagereport;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.gs.incode.services.stringtransformation.persistance.exporters.Exporter;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;

abstract class BaseTransformerUsageReportExporter implements Exporter<TransformerUsageReport> {

  static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

  protected Map<String, String> toMap(TransformerUsageReport report) {

    Map<String, String> result = new LinkedHashMap<>();

    result.put("FROM", report.getFrom().format(DEFAULT_DATE_FORMAT));
    result.put("TO", report.getTo().format(DEFAULT_DATE_FORMAT));
    result.put("TOTAL_TRANSFORMATIONS", String.valueOf(report.getTotalTransformationCounter()));
    result.put("FAILED_TRANSFORMATIONS", String.valueOf(report.getFailedCounter()));
    result.put("SUCCESS_TRANSFORMATIONS", String.valueOf(report.getSuccessCounter()));
    result.put("TOTAL_TRANSFORMERS", String.valueOf(report.getTotalTransformerCounter()));
    report.getStatistic().forEach((k, v) -> result.put(k.name(), String.valueOf(v)));
    return result;
  }
}
