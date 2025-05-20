package org.gs.incode.services.stringtransformation.container;

import org.gs.incode.services.stringtransformation.application.usecases.TransformStringUsecase;
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
}
