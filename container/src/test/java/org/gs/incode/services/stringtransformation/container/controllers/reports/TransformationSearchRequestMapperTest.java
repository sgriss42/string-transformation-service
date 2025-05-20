package org.gs.incode.services.stringtransformation.container.controllers.reports;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class TransformationSearchRequestMapperTest {
  TransformationSearchRequestMapper mapper = new TransformationSearchRequestMapper();

  @Test
  void whenValidRequestThenMapToSearchQuery() {
    LocalDateTime from = LocalDateTime.of(2024, 5, 1, 10, 0, 0, 0);
    LocalDateTime to = LocalDateTime.of(2024, 5, 2, 10, 0, 0, 0);
    Pageable pageable = PageRequest.of(1, 20);

    TransformationSearchRequest request = new TransformationSearchRequest();
    request.setFrom(from);
    request.setTo(to);

    TransformationSearchQuery query =
        mapper.searchRequestToTransformationSearchQuery(request, pageable);

    assertEquals(from.toInstant(ZoneOffset.UTC), query.getFrom());
    assertEquals(to.toInstant(ZoneOffset.UTC), query.getTo());
    assertEquals(1, query.getPage());
    assertEquals(20, query.getSize());
  }

  @Test
  void whenRequestIsNullThenThrowException() {
    Pageable pageable = PageRequest.of(0, 10);

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> mapper.searchRequestToTransformationSearchQuery(null, pageable));
    assertEquals("Request must not be null!", ex.getMessage());
  }

  @Test
  void whenPageableIsNullThenThrowException() {
    TransformationSearchRequest request = new TransformationSearchRequest();
    request.setFrom(LocalDateTime.now());
    request.setTo(LocalDateTime.now());

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> mapper.searchRequestToTransformationSearchQuery(request, null));
    assertEquals("Pageable must not be null!", ex.getMessage());
  }
}
