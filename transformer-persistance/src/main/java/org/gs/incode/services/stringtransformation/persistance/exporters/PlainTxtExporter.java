package org.gs.incode.services.stringtransformation.persistance.exporters;

import java.io.OutputStream;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;

public class PlainTxtExporter implements Exporter<TransformerUsageReport> {
  @Override
  public void export(TransformerUsageReport report, OutputStream stream) {}
}
