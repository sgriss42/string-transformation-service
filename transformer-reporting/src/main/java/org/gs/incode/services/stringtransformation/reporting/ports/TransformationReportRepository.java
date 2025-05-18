package org.gs.incode.services.stringtransformation.reporting.ports;

import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;

public interface TransformationReportRepository {
  void save(TransformationJobReport report);

  PagedResponse<TransformationResult> findAll(TransformationSearchQuery query);
}
