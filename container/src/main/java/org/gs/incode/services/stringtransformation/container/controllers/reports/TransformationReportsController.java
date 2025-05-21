package org.gs.incode.services.stringtransformation.container.controllers.reports;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.gs.incode.services.stringtransformation.application.usecases.DownloadReportQuery;
import org.gs.incode.services.stringtransformation.application.usecases.DownloadTransformerUsageReportUsecase;
import org.gs.incode.services.stringtransformation.application.usecases.GetTransformationsUsecase;
import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class TransformationReportsController {

  private final GetTransformationsUsecase getTransformationsUsecase;
  private final DownloadTransformerUsageReportUsecase downloadTransformerUsageReportUsecase;
  private final TransformationSearchRequestMapper mapper;

  public TransformationReportsController(
      GetTransformationsUsecase getTransformationsUsecase,
      DownloadTransformerUsageReportUsecase downloadTransformerUsageReportUsecase,
      TransformationSearchRequestMapper mapper) {
    this.getTransformationsUsecase = getTransformationsUsecase;
    this.downloadTransformerUsageReportUsecase = downloadTransformerUsageReportUsecase;
    this.mapper = mapper;
  }

  @GetMapping("/transformations")
  @Parameters({
    @Parameter(
        in = ParameterIn.QUERY,
        description = "Zero-based page index (0..N)",
        name = "page",
        schema = @Schema(type = "integer", defaultValue = "0")),
    @Parameter(
        in = ParameterIn.QUERY,
        description = "The size of the page to be returned",
        name = "size",
        schema = @Schema(type = "integer", defaultValue = "20")),
    @Parameter(hidden = true, in = ParameterIn.QUERY, name = "sort")
  })
  public PagedResponse<TransformationResult> findTransformations(
      @Valid TransformationSearchRequest transformationSearchRequest,
      @Parameter(hidden = true) @PageableDefault(page = 0, size = 10) Pageable pageable) {
    TransformationSearchQuery query =
        mapper.searchRequestToTransformationSearchQuery(transformationSearchRequest, pageable);
    return getTransformationsUsecase.execute(query);
  }

  @GetMapping("/transformations/report")
  public void downloadCsv(
      HttpServletResponse response, @Valid DownloadReportRequest downloadReportRequest)
      throws IOException {

    prepareHeaders(response, downloadReportRequest);
    DownloadReportQuery query =
        mapper.transformationReportRequestToDownloadReportQuery(
            downloadReportRequest, response.getOutputStream());
    downloadTransformerUsageReportUsecase.execute(query);
  }

  private void prepareHeaders(
      HttpServletResponse response, DownloadReportRequest downloadReportRequest) {
    response.setContentType("application/octet-stream");
    response.setHeader(
        "Content-Disposition", "attachment; filename=\"" + downloadReportRequest.name() + "\"");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
  }
}
