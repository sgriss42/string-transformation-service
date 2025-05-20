package org.gs.incode.services.stringtransformation.persistance.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import org.gs.incode.services.stringtransformation.persistance.exceptions.UnsupportedExporter;
import org.gs.incode.services.stringtransformation.persistance.exporters.Exporter;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;
import org.junit.jupiter.api.Test;

class TransformerUsageReportExporterAdapterTest {

  private Exporter<TransformerUsageReport> mockCsvExporter = mock(Exporter.class);
  private Exporter<TransformerUsageReport> mockTextExporter = mock(Exporter.class);
  private TransformerUsageReportExporterAdapter adapter =
      new TransformerUsageReportExporterAdapter(
          Map.of("CSV", mockCsvExporter, "TEXT", mockTextExporter));

  @Test
  void whenValidFormatThenUsesCorrectExporter() {

    TransformerUsageReport mockReport = mock(TransformerUsageReport.class);
    ByteArrayOutputStream mockOutPutStream = mock(ByteArrayOutputStream.class);
    adapter.export(mockReport, "CSV", mockOutPutStream);

    verify(mockCsvExporter).export(mockReport, mockOutPutStream);
    verifyNoInteractions(mockTextExporter);
  }

  @Test
  void whenUnsupportedFormatThenThrowsException() {
    TransformerUsageReport mockReport = mock(TransformerUsageReport.class);
    ByteArrayOutputStream mockOutPutStream = mock(ByteArrayOutputStream.class);

    UnsupportedExporter ex =
        assertThrows(
            UnsupportedExporter.class,
            () -> {
              adapter.export(mockReport, "XML", mockOutPutStream);
            });

    assertTrue(ex.getMessage().contains("XML"));
  }
}
