package org.gs.incode.services.stringtransformation.reporting.ports;

import java.time.Instant;
import java.util.List;
import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.gs.incode.services.stringtransformation.reporting.TransformationResultWithTransformers;

public interface TransformationReportRepository {
  void save(TransformationJobReport report);

  PagedResponse<TransformationResult> findAll(TransformationSearchQuery query);

  List<TransformationResultWithTransformers> findAllForTimeRange(Instant from, Instant to);
}
