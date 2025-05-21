package org.gs.incode.services.stringtransformation.persistance.jpa.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.gs.incode.services.stringtransformation.dtos.TransformerType;

@Getter
@Setter
@Table(name = "transaction_task")
@IdClass(JpaTransformationTaskId.class)
@Entity
public class JpaTransformerTask {
  @Id private Integer id;

  @Id
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "JOB_ID")
  JpaTransactionJob job;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransformerType type;

  @Column(columnDefinition = "TEXT")
  private String parameters;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JpaTransformerTask that = (JpaTransformerTask) o;
    return Objects.equals(id, that.id) && Objects.equals(job, that.job);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, job);
  }
}
