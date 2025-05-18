package org.gs.incode.services.stringtransformation.dtos;

import java.util.List;
import lombok.Value;

@Value
public class Page<T> {
  private final List<T> data;
  private final int total;
  private final int offset;
  private final int size;

  public Page(List<T> data, int total, int offset, int size) {
    this.data = data;
    this.total = total;
    this.offset = offset;
    this.size = size;
  }
}
