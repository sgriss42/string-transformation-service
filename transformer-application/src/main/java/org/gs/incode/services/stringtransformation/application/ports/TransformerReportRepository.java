package org.gs.incode.services.stringtransformation.application.ports;

import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;

public interface TransformerReportRepository {
  void save(TransformationJobReport report);
}
