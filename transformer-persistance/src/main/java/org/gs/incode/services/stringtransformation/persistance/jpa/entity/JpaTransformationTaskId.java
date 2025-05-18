package org.gs.incode.services.stringtransformation.persistance.jpa.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JpaTransformationTaskId implements Serializable {

  private Long id;

  private JpaTransactionJob execution;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JpaTransformationTaskId that = (JpaTransformationTaskId) o;
    return Objects.equals(id, that.id) && Objects.equals(execution, that.execution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, execution);
  }
}
