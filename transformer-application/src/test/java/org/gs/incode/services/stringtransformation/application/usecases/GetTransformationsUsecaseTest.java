package org.gs.incode.services.stringtransformation.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import org.gs.incode.services.stringtransformation.dtos.PagedResponse;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.gs.incode.services.stringtransformation.reporting.TransformationResult;
import org.gs.incode.services.stringtransformation.reporting.ports.TransformationReportRepository;
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
    TransformationResult result1 =
        new TransformationResult(Instant.now(), Instant.now().plusMillis(100), "id1", true, null);
    TransformationResult result2 =
        new TransformationResult(
            Instant.now(), Instant.now().plusMillis(100), "id2", false, "some error");

    PagedResponse<TransformationResult> expectedPagedResponse =
        new PagedResponse<>(List.of(result1, result2), 0, 0, 0);

    when(repository.findAll(query)).thenReturn(expectedPagedResponse);

    PagedResponse<TransformationResult> resultPagedResponse = usecase.execute(query);

    assertNotNull(resultPagedResponse);
    assertEquals("id1", resultPagedResponse.getContent().get(0).getId());
    verify(repository).findAll(query);
  }

  @Test
  void whenQueryIsNullThanThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> usecase.execute(null));
  }
}
