package org.gs.incode.services.stringtransformation.application.transformation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import org.gs.incode.services.stringtransformation.application.ports.TransformerReportRepository;
import org.gs.incode.services.stringtransformation.dtos.TransformationCommand;
import org.gs.incode.services.stringtransformation.dtos.TransformationResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;
import org.gs.incode.services.stringtransformation.job.TransformationJob;
import org.gs.incode.services.stringtransformation.reporting.TransformationJobReport;
import org.gs.incode.services.stringtransformation.transformers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransformStringUsecaseTest {

  @Mock private TransformerFactory mockTransformerFactory;
  @Mock private TransformerReportRepository mockTransformerReportRepository;
  private TransformStringUsecase usecase;

  @BeforeEach
  void setUp() {
    usecase = new TransformStringUsecase(mockTransformerFactory, mockTransformerReportRepository);
  }

  @Test
  void whenCompletedSuccessfullyThanReturnsResponse() {
    String input = "test";
    String expectedResult = "TEST";

    TransformationCommand command =
        new TransformationCommand(input, List.of(mock(TransformerTaskConfig.class)));
    when(mockTransformerFactory.construct(any())).thenReturn(new UppercaseTransformerTask());

    TransformationResponse response = usecase.execute(command);
    assertNotNull(response.id());
    assertTrue(response.isOk());
    assertEquals(expectedResult, response.result());
    assertNull(response.errorMessages());
    verify(mockTransformerReportRepository).save(any(TransformationJobReport.class));
  }

  @Test
  void whenFailedDuringTransformationThanReturnsResponse() {
    String input = "test";

    TransformationCommand command =
        new TransformationCommand(input, List.of(mock(TransformerTaskConfig.class)));
    TransformerTask failed =
        (s) -> {
          throw new IllegalArgumentException();
        };
    when(mockTransformerFactory.construct(any())).thenReturn(failed);

    TransformationResponse response = usecase.execute(command);
    assertNotNull(response.id());
    assertFalse(response.isOk());
    assertNull(response.result());
    assertTrue(response.errorMessages().startsWith("Transformation task #0"));
    verify(mockTransformerReportRepository).save(any(TransformationJobReport.class));
  }

  @Test
  void whenFailedDuringJobCreationThanReturnsResponse() {
    String input = "test";

    TransformationCommand command =
        new TransformationCommand(input, List.of(mock(TransformerTaskConfig.class)));
    when(mockTransformerFactory.construct(any()))
        .thenThrow(new RuntimeException("some problem during  job construction"));

    TransformationResponse response = usecase.execute(command);
    assertNotNull(response.id());
    assertFalse(response.isOk());
    assertNull(response.result());
    assertTrue(response.errorMessages().endsWith("some problem during  job construction"));
    verify(mockTransformerReportRepository).save(any(TransformationJobReport.class));
  }

  @Test
  void whenPrepareJobCalledWithProperInputThanReturnsTransformationJob() {

    TransformationCommand command =
        new TransformationCommand(
            "some text",
            List.of(
                mock(TransformerTaskConfig.class),
                mock(TransformerTaskConfig.class),
                mock(TransformerTaskConfig.class)));
    when(mockTransformerFactory.construct(any())).thenReturn(new UppercaseTransformerTask());
    TransformationJob transformationJob = usecase.prepareJob(command);
    assertNotNull(transformationJob);
    assertEquals("some text", transformationJob.getInput());
    assertEquals(3, transformationJob.getTransformerTasks().size());
    verify(mockTransformerFactory, times(3)).construct(any(TransformerTaskConfig.class));
  }
}
