package org.gs.incode.services.stringtransformation.container.controllers.transformation;

import java.util.List;
import org.gs.incode.services.stringtransformation.dtos.TransformationCommand;
import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;
import org.springframework.stereotype.Component;

@Component
class TransformationRequestMapper {
  TransformationCommand transformationRequestToTransformationCommand(
      TransformationRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Request must not be null!");
    }

    if (request.getTransformerConfigurations() == null
        || request.getTransformerConfigurations().isEmpty()) {
      throw new IllegalArgumentException("Transformers Configs must not be null or empty!");
    }

    List<TransformerTaskConfig> transformerTaskConfigs =
        request.getTransformerConfigurations().stream()
            .map(
                e -> {
                  TransformerTaskConfig transformerTaskConfig =
                      TransformerTaskConfig.of(e.getType());

                  transformerTaskConfig.replacement(e.getReplacement());
                  transformerTaskConfig.regexp(e.getRegexp());
                  transformerTaskConfig.locale(e.getLocale());

                  return transformerTaskConfig;
                })
            .toList();
    return new TransformationCommand(request.getInput(), transformerTaskConfigs);
  }
}
