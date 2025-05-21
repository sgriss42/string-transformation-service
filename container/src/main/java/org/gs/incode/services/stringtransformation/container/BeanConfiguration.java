package org.gs.incode.services.stringtransformation.container;

import java.util.Map;
import org.gs.incode.services.stringtransformation.application.usecases.DownloadTransformerUsageReportUsecase;
import org.gs.incode.services.stringtransformation.application.usecases.GetTransformationsUsecase;
import org.gs.incode.services.stringtransformation.application.usecases.TransformStringUsecase;
import org.gs.incode.services.stringtransformation.persistance.adapters.TransformerUsageReportExporterAdapter;
import org.gs.incode.services.stringtransformation.persistance.exporters.transformersusagereport.CsvExporter;
import org.gs.incode.services.stringtransformation.persistance.exporters.transformersusagereport.PlainTxtExporter;
import org.gs.incode.services.stringtransformation.reporting.TransformerUsageReport;
import org.gs.incode.services.stringtransformation.reporting.ports.ReportExporter;
import org.gs.incode.services.stringtransformation.reporting.ports.TransformationReportRepository;
import org.gs.incode.services.stringtransformation.transformers.DefaultTransformerFactory;
import org.gs.incode.services.stringtransformation.transformers.TransformerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
  @Bean
  TransformerFactory transformerFactory() {
    return new DefaultTransformerFactory();
  }

  @Bean
  TransformStringUsecase transformStringUsecase(
      TransformerFactory transformerFactory,
      TransformationReportRepository transformationReportRepository) {
    return new TransformStringUsecase(transformerFactory, transformationReportRepository);
  }

  @Bean
  GetTransformationsUsecase getTransformationsUsecase(
      TransformationReportRepository transformationReportRepository) {
    return new GetTransformationsUsecase(transformationReportRepository);
  }

  @Bean
  DownloadTransformerUsageReportUsecase downloadTransformerUsageReportUsecase(
      TransformationReportRepository transformationReportRepository,
      ReportExporter<TransformerUsageReport> transformerUsageReportExporter) {

    return new DownloadTransformerUsageReportUsecase(
        transformationReportRepository, transformerUsageReportExporter);
  }

  @Bean
  ReportExporter<TransformerUsageReport> transformerUsageReportExporter() {
    return new TransformerUsageReportExporterAdapter(
        Map.of("CSV", new CsvExporter(), "TXT", new PlainTxtExporter()));
  }
}
