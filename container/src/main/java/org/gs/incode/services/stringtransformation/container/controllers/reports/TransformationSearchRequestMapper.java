package org.gs.incode.services.stringtransformation.container.controllers.reports;

import java.time.ZoneOffset;
import org.gs.incode.services.stringtransformation.dtos.TransformationSearchQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class TransformationSearchRequestMapper {
  public TransformationSearchQuery searchRequestToTransformationSearchQuery(
      TransformationSearchRequest transformationSearchRequest, Pageable pageable) {

    if (transformationSearchRequest == null) {
      throw new IllegalArgumentException("Request must not be null!");
    }
    if (pageable == null) {
      throw new IllegalArgumentException("Pageable must not be null!");
    }

    TransformationSearchQuery query = new TransformationSearchQuery();
    query.setFrom(transformationSearchRequest.getFrom().toInstant(ZoneOffset.UTC));
    query.setTo(transformationSearchRequest.getTo().toInstant(ZoneOffset.UTC));
    query.setPage(pageable.getPageNumber());
    query.setSize(pageable.getPageSize());
    return query;
  }
}
