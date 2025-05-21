package org.gs.incode.services.stringtransformation.application.usecases;

import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gs.incode.services.stringtransformation.application.usecases.dto.DownloadReportQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationResultWithTransformers;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;
import org.gs.incode.services.stringtransformation.reporting.ports.ReportExporter;
import org.gs.incode.services.stringtransformation.reporting.ports.TransformationReportRepository;

@Slf4j
public class DownloadTransformerUsageReportUsecase {
  private final TransformationReportRepository repository;
  private final ReportExporter<TransformerUsageReport> exporter;

  public DownloadTransformerUsageReportUsecase(
      TransformationReportRepository repository, ReportExporter<TransformerUsageReport> exporter) {
    this.repository = repository;
    this.exporter = exporter;
  }

  public void execute(@Valid DownloadReportQuery query) {
    log.info("Report requested {}", query);
    TransformerUsageReport transformerUsageReport =
        new TransformerUsageReport(query.getFrom(), query.getTo());
    List<TransformationResultWithTransformers> allForTimeRange =
        repository.findAllForTimeRange(query.getFrom().toInstant(), query.getTo().toInstant());
    transformerUsageReport.addToStatistic(allForTimeRange);
    exporter.export(transformerUsageReport, query.getFormat(), query.getOutputStream());
  }
}
