package org.gs.incode.services.stringtransformation.persistance.exporters;

import java.io.OutputStream;
import java.io.PrintWriter;
import org.apache.commons.text.StringSubstitutor;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;

public class PlainTxtExporter implements Exporter<TransformerUsageReport> {
  private static final  String DEFAULT_TEMPLATE =
      """
  Report for
  \t\tFROM        :${FROM}
  \t\tTO          :${FROM}


  TRANSFORMATIONS\t: ${TOTAL_TRANSFORMATIONS}\t (Success: ${SUCCESS_TRANSFORMATIONS}; Failed:${FAILED_TRANSFORMATIONS})
  TRANSFORMERS\t\t: ${TOTAL_TRANSFORMERS}
  \t\tUPPERCASE   : ${TO_UPPERCASE}
  \t\tLOWERCASE   : ${TO_LOWERCASE}
  \t\tREPLACE     : ${REGEXP_REPLACE}
  \t\tDELETE      : ${REGEXP_DELETE}
""";

  @Override
  public void export(TransformerUsageReport report, OutputStream stream) {
    String formatted = StringSubstitutor.replace(DEFAULT_TEMPLATE, report.asMap(), "${", "}");
    try (PrintWriter writer = new PrintWriter(stream)) {
      writer.println(formatted);
      writer.flush();
    }
  }
}
