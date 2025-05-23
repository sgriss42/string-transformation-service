package org.gs.incode.services.stringtransformation.dtos;

import java.util.List;
import lombok.Value;

@Value
public class PagedResponse<T> {
  private final List<T> content;
  private final long totalElements;
  private final int page;
  private final int size;

  public PagedResponse(List<T> content, long totalElements, int page, int size) {
    this.content = content;
    this.totalElements = totalElements;
    this.page = page;
    this.size = size;
  }
}
