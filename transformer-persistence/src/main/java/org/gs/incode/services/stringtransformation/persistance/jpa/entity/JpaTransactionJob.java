package org.gs.incode.services.stringtransformation.persistance.jpa.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "transaction_jobs")
@Entity
public class JpaTransactionJob {
  @Id private UUID id;

  @Column(length = 10_000, nullable = false, updatable = false)
  private String input;

  @Column(length = 10_000, updatable = false)
  private String result;

  @Column(name = "error_message", length = 500, updatable = false)
  private String errorMessage;

  @Column(name = "is_job_completed_successfully", updatable = false)
  private Boolean isJobCompletedSuccessfully;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<JpaTransformerTask> transformers = new ArrayList<>();

  @Column(name = "completed_at", updatable = false)
  private Instant completedAt;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    JpaTransactionJob that = (JpaTransactionJob) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
