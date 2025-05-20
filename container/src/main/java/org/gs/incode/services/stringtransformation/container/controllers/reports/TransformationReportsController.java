package org.gs.incode.services.stringtransformation.container.controllers.reports;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.gs.incode.services.stringtransformation.application.usecases.GetTransformationsUsecase;
import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class TransformationReportsController {

  private final GetTransformationsUsecase usecase;
  private final TransformationSearchRequestMapper mapper;

  public TransformationReportsController(
      GetTransformationsUsecase usecase, TransformationSearchRequestMapper mapper) {
    this.usecase = usecase;
    this.mapper = mapper;
  }

  @GetMapping("/transformations")
  public PagedResponse<TransformationResult> findTransformations(
      @Valid TransformationSearchRequest transformationSearchRequest, Pageable pageable) {
    TransformationSearchQuery query =
        mapper.searchRequestToTransformationSearchQuery(transformationSearchRequest, pageable);
    return usecase.execute(query);
  }
}
