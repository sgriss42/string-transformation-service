package org.gs.incode.services.stringtransformation.persistance.exporters;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.gs.incode.services.stringtransformation.persistance.exceptions.ExporterException;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;

public class CsvExporter implements Exporter<TransformerUsageReport> {
  @Override
  public void export(TransformerUsageReport report, OutputStream stream) {

    String[] csvHeader = {
      "Field", "Value",
    };

    List<List<String>> csvBody = new ArrayList<>();
    Map<String, String> stringStringMap = report.asMap();
    stringStringMap.forEach((key, value) -> csvBody.add(List.of(key, value)));
    try (CSVPrinter csvPrinter =
        new CSVPrinter(
            new PrintWriter(stream, false, StandardCharsets.UTF_8),
            CSVFormat.DEFAULT.withHeader(csvHeader))) {
      for (List<String> line : csvBody) {
        csvPrinter.printRecord(line);
      }

      csvPrinter.flush();
    } catch (IOException e) {
      throw new ExporterException(e.getMessage(), e);
    }
  }
}
