package org.gs.incode.services.stringtransformation.container.controllers.transformation;

import static org.gs.incode.services.stringtransformation.dtos.TransformerType.REGEXP_REPLACE;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.gs.incode.services.stringtransformation.dtos.TransformationCommand;
import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;
import org.junit.jupiter.api.Test;

class TransformationRequestMapperTest {

  private final TransformationRequestMapper mapper = new TransformationRequestMapper();

  @Test
  void whenValidRequestThenMapToTransformationCommand() {

    TransformationRequest.TransformerConfiguration task =
        new TransformationRequest.TransformerConfiguration();
    task.setType(REGEXP_REPLACE);
    task.setReplacement("a");
    task.setRegexp("b");
    task.setLocale("c");

    TransformationRequest request = new TransformationRequest();
    request.setInput("hello");
    request.setTransformerConfigurations(List.of(task));

    TransformationCommand command = mapper.transformationRequestToTransformationCommand(request);

    assertEquals("hello", command.input());
    assertEquals(1, command.transformerTaskConfigs().size());

    TransformerTaskConfig config = command.transformerTaskConfigs().get(0);
    assertEquals(REGEXP_REPLACE, config.getType());
    assertEquals("a", config.replacement());
    assertEquals("b", config.regexp());
    assertEquals("c", config.locale());
  }

  @Test
  void whenRequestIsNullThenThrowException() {
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> mapper.transformationRequestToTransformationCommand(null));
    assertEquals("Request must not be null!", ex.getMessage());
  }

  @Test
  void whenTransformersIsNullThenThrowException() {
    TransformationRequest request = new TransformationRequest();
    request.setInput("text");
    request.setTransformerConfigurations(null);

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> mapper.transformationRequestToTransformationCommand(request));
    assertEquals("Transformers Configs must not be null or empty!", ex.getMessage());
  }
}
