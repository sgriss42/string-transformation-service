package org.gs.incode.services.stringtransformation.application.usecases;

import lombok.extern.slf4j.Slf4j;
import org.gs.incode.services.stringtransformation.dtos.TransformationCommand;
import org.gs.incode.services.stringtransformation.dtos.TransformationResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;
import org.gs.incode.services.stringtransformation.job.Builder;
import org.gs.incode.services.stringtransformation.job.TransformationJob;
import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;
import org.gs.incode.services.stringtransformation.reporting.ports.TransformationReportRepository;
import org.gs.incode.services.stringtransformation.transformers.TransformerFactory;

@Slf4j
public class TransformStringUsecase {
  private final TransformerFactory transformerFactory;
  private final TransformationReportRepository transformationReportRepository;

  public TransformStringUsecase(
      TransformerFactory transformerFactory,
      TransformationReportRepository transformationReportRepository) {
    this.transformerFactory = transformerFactory;
    this.transformationReportRepository = transformationReportRepository;
  }

  public TransformationResponse execute(TransformationCommand command) {
    log.info("Executing transformation for command: {}", command);

    TransformationJobReport report = prepareReport(command);
    try {
      TransformationJob job = prepareJob(command);
      job.execute();

      if (job.isSuccess()) {
        log.info("Transformation succeeded with result: {}", job.getResult());
        report.success(job.getResult());
      } else {
        log.warn("Transformation failed with error: {}", job.getError());
        report.failed(job.getError());
      }
    } catch (Exception e) {
      log.error("Unexpected error during transformation", e);
      report.failed("Unexpected error: " + e.getMessage());
    }

    transformationReportRepository.save(report);
    return prepareTransformResponse(report);
  }

  private TransformationResponse prepareTransformResponse(TransformationJobReport report) {
    return new TransformationResponse(
        report.getId(),
        report.getResult(),
        report.getErrorMessages(),
        report.getIsJobCompletedSuccessfully());
  }

  private TransformationJobReport prepareReport(TransformationCommand command) {
    TransformationJobReport transformationJobReport = new TransformationJobReport();
    transformationJobReport.initializeReport(command);
    return transformationJobReport;
  }

  protected TransformationJob prepareJob(TransformationCommand command) {
    Builder builder = TransformationJob.builder().input(command.input());
    for (TransformerTaskConfig conf : command.transformerTaskConfigs()) {
      builder.addTransformerTask(transformerFactory.construct(conf));
    }
    return builder.build();
  }
}
