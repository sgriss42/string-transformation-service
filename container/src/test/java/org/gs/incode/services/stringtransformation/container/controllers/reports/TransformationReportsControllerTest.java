package org.gs.incode.services.stringtransformation.container.controllers.reports;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.gs.incode.services.stringtransformation.application.usecases.GetTransformationsUsecase;
import org.gs.incode.services.stringtransformation.container.controllers.transformation.TransformationRequest;
import org.gs.incode.services.stringtransformation.container.exceptionhandler.ApiExceptionHandler;
import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class TransformationReportsControllerTest {

  public static final String API_V_1_TRANSFORMATIONS = "/api/v1/transformations";
  private MockMvc mockMvc;
  @InjectMocks TransformationReportsController controller;
  private ObjectMapper objectMapper;

  @Mock private GetTransformationsUsecase mockUsecase;

  @Mock private TransformationSearchRequestMapper mockMapper;

  @BeforeEach
  void setup() {
    objectMapper = JsonMapper.builder().build();
    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
  }

  @Test
  void whenValidQueryParamsThenReturns200() throws Exception {
    PagedResponse<TransformationResult> response =
        new PagedResponse<>(List.of(mock(TransformationResult.class)), 1, 0, 11);

    doReturn(mock(TransformationSearchQuery.class))
        .when(mockMapper)
        .searchRequestToTransformationSearchQuery(any(), any());
    doReturn(response).when(mockUsecase).execute(any());

    mockMvc
        .perform(
            get(API_V_1_TRANSFORMATIONS)
                .param("from", LocalDateTime.now().minusDays(1).toString())
                .param("to", LocalDateTime.now().toString())
                .param("page", "0")
                .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void whenInvalidInputThenReturnBadRequest() throws Exception {
    TransformationRequest request = new TransformationRequest();
    mockMvc
        .perform(
            get(API_V_1_TRANSFORMATIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
