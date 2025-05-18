package org.gs.incode.services.stringtransformation.application.usecases;

import lombok.extern.slf4j.Slf4j;
import org.gs.incode.services.stringtransformation.application.ports.TransformationReportRepository;
import org.gs.incode.services.stringtransformation.dtos.Page;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;

@Slf4j
public class GetTransformationsUsecase {

  private final TransformationReportRepository repository;

  public GetTransformationsUsecase(TransformationReportRepository repository) {
    this.repository = repository;
  }

  public Page<TransformationResult> execute(TransformationSearchQuery query) {
    if (query == null) {
      throw new IllegalArgumentException("Query can not be null!");
    }
    log.info("Request transforms for {}", query);
    Page<TransformationResult> transformations = repository.findAll(query);
    log.info("--> found {}", transformations);
    return transformations;
  }
}
