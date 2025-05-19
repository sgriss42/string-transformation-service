package org.gs.incode.services.stringtransformation.persistance.adapters;

import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.gs.incode.services.stringtransformation.persistance.exceptions.UnsupportedExporter;
import org.gs.incode.services.stringtransformation.persistance.exporters.CsvExporter;
import org.gs.incode.services.stringtransformation.persistance.exporters.Exporter;
import org.gs.incode.services.stringtransformation.persistance.exporters.PlainTxtExporter;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;
import org.gs.incode.services.stringtransformation.reporting.ports.ReportExporter;

@Slf4j
public class TransformerUsageReportExporterAdapter
    implements ReportExporter<TransformerUsageReport> {

  private final Map<String, Exporter<TransformerUsageReport>> availableExporters;

  public TransformerUsageReportExporterAdapter() {
    availableExporters = Map.of("CSV", new CsvExporter(), "TEXT", new PlainTxtExporter());
  }

  @Override
  public void export(TransformerUsageReport report, String format, OutputStream outputStream) {
    Objects.requireNonNull(report, "Report shouldn't be null");
    Objects.requireNonNull(format, "Format shouldn't be null");
    log.info("Starting export {} to {} ", report, format);
    Exporter<TransformerUsageReport> exporter = getExporter(format);
    exporter.export(report, outputStream);
    log.info(" --> done");
  }

  private Exporter<TransformerUsageReport> getExporter(String format) {
    if (availableExporters.containsKey(format)) {
      throw new UnsupportedExporter(format, availableExporters.keySet());
    }
    return availableExporters.get(format);
  }
}
