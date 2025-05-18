package org.gs.incode.services.stringtransformation.persistance.jpa.repository;

import java.time.Instant;
import java.util.UUID;
import org.gs.incode.services.stringtransformation.persistance.jpa.entity.JpaTransactionJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransformationJobRepository extends JpaRepository<JpaTransactionJob, UUID> {

  Page<JpaTransactionJob> findAllByCompletedAtBetween(Instant from, Instant to, Pageable pageable);
}
