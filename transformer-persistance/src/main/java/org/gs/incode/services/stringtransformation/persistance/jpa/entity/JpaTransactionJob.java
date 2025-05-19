package org.gs.incode.services.stringtransformation.persistance.jpa.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "transaction_jobs")
@Entity
public class JpaTransactionJob implements Serializable {
  @Id private UUID id;

  @Column(length = 10_000, nullable = false)
  private String input;

  @Lob @Column private String result;

  @Column(name = "error_message", length = 500)
  private String errorMessage;

  @Column(name = "is_job_completed_successfully")
  private Boolean isJobCompletedSuccessfully;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<JpaTransformerTask> transformers = new ArrayList<>();

  // TODO: validation  should be gt than createdAt
  @Column(name = "completed_at")
  private Instant completedAt;
}
