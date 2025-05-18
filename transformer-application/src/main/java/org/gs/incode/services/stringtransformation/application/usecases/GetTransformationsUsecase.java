package org.gs.incode.services.stringtransformation.application.usecases;

import lombok.extern.slf4j.Slf4j;
import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.gs.incode.services.stringtransformation.reporting.ports.TransformationReportRepository;

@Slf4j
public class GetTransformationsUsecase {

  private final TransformationReportRepository repository;

  public GetTransformationsUsecase(TransformationReportRepository repository) {
    this.repository = repository;
  }

  public PagedResponse<TransformationResult> execute(TransformationSearchQuery query) {
    if (query == null) {
      throw new IllegalArgumentException("Query can not be null!");
    }
    log.info("Request transforms for {}", query);
    PagedResponse<TransformationResult> transformations = repository.findAll(query);
    log.info("--> found {}", transformations);
    return transformations;
  }
}
