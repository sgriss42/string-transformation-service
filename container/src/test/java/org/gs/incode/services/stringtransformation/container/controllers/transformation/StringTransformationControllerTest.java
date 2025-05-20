package org.gs.incode.services.stringtransformation.container.controllers.transformation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.List;
import org.gs.incode.services.stringtransformation.application.usecases.TransformStringUsecase;
import org.gs.incode.services.stringtransformation.container.exceptionhandler.ApiExceptionHandler;
import org.gs.incode.services.stringtransformation.dtos.TransformationCommand;
import org.gs.incode.services.stringtransformation.dtos.TransformationResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class StringTransformationControllerTest {

  public static final String API_V_1_TRANSFORMATIONS = "/api/v1/transformations";
  private MockMvc mockMvc;
  @InjectMocks StringTransformationController controller;
  private ObjectMapper objectMapper;

  @Mock private TransformStringUsecase usecase;

  @Mock private TransformationRequestMapper mapper;

  @BeforeEach
  public void setup() {
    objectMapper = JsonMapper.builder().build();
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
  }

  @Test
  void whenValidRequestThenReturnTransformedString() throws Exception {
    TransformationRequest request = new TransformationRequest();
    request.setInput("hello");
    TransformationRequest.TransformerConfiguration transformerConfiguration =
        new TransformationRequest.TransformerConfiguration();
    transformerConfiguration.setType(TransformerType.TO_UPPERCASE);
    request.setTransformerConfigurations(List.of(transformerConfiguration));

    TransformationResponse response = new TransformationResponse("uuid", "HELLO", null, true);

    doReturn(mock(TransformationCommand.class))
        .when(mapper)
        .transformationRequestToTransformationCommand(any());
    doReturn(response).when(usecase).execute(any(TransformationCommand.class));

    mockMvc
        .perform(
            post(API_V_1_TRANSFORMATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value("HELLO"))
        .andExpect(jsonPath("$.isOk").value("true"))
        .andExpect(jsonPath("$.id").isNotEmpty());
  }

  @Test
  void whenInvalidInputThenReturnBadRequest() throws Exception {
    TransformationRequest request = new TransformationRequest();
    mockMvc
        .perform(
            post(API_V_1_TRANSFORMATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
