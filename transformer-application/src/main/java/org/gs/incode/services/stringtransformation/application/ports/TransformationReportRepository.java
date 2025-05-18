package org.gs.incode.services.stringtransformation.application.ports;

import org.gs.incode.services.stringtransformation.dtos.Page;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;

public interface TransformationReportRepository {
  void save(TransformationJobReport report);

  Page<TransformationResult> findAll(TransformationSearchQuery query);
}
