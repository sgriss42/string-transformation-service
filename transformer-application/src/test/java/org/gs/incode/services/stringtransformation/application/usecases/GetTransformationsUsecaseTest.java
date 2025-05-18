package org.gs.incode.services.stringtransformation.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import org.gs.incode.services.stringtransformation.application.ports.TransformationReportRepository;
import org.gs.incode.services.stringtransformation.dtos.Page;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTransformationsUsecaseTest {

  @Mock private TransformationReportRepository repository;

  private GetTransformationsUsecase usecase;

  @BeforeEach
  void setUp() {
    usecase = new GetTransformationsUsecase(repository);
  }

  @Test
  void whenQueryIsOkThanReturnResult() {
    TransformationSearchQuery query = new TransformationSearchQuery();
    TransformationResult result1 = new TransformationResult(Instant.now(), "id1", true, null);
    TransformationResult result2 =
        new TransformationResult(Instant.now(), "id2", false, "some error");

    Page<TransformationResult> expectedPage = new Page<>(List.of(result1, result2), 0, 0, 0);

    when(repository.findAll(query)).thenReturn(expectedPage);

    Page<TransformationResult> resultPage = usecase.execute(query);

    assertNotNull(resultPage);
    assertEquals("id1", resultPage.getData().get(0).getId());
    verify(repository).findAll(query);
  }

  @Test
  void whenQueryIsNullThanThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> usecase.execute(null));
  }
}
