package org.gs.incode.services.stringtransformation.container.controllers.transformation;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.gs.incode.services.stringtransformation.application.usecases.TransformStringUsecase;
import org.gs.incode.services.stringtransformation.dtos.TransformationCommand;
import org.gs.incode.services.stringtransformation.dtos.TransformationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class StringTransformationController {
  private final TransformStringUsecase usecase;
  private final TransformationRequestMapper mapper;

  public StringTransformationController(
      TransformStringUsecase usecase, TransformationRequestMapper mapper) {
    this.usecase = usecase;
    this.mapper = mapper;
  }

  @PostMapping("/transformations")
  TransformationResponse transform(@Valid @RequestBody TransformationRequest request) {
    TransformationCommand transformationCommand =
        mapper.transformationRequestToTransformationCommand(request);
    return usecase.execute(transformationCommand);
  }
}
