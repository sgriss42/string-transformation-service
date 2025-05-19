package org.gs.incode.services.stringtransformation.reporting.ports;

import java.io.OutputStream;

public interface ReportExporter<T> {
  /**
   * Attention this method does not close the stream!
   *
   * @param report report for exporting
   * @param format target format
   * @param outputStream stream for writing results
   */
  void export(T report, String format, OutputStream outputStream);
}
