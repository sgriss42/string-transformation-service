package org.gs.incode.services.stringtransformation.persistance.jpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.stream.IntStream;

import org.gs.incode.services.stringtransformation.dtos.TransformerType;
import org.gs.incode.services.stringtransformation.persistance.jpa.entity.JpaTransactionJob;
import org.gs.incode.services.stringtransformation.persistance.jpa.entity.JpaTransformerTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@ContextConfiguration(classes = JpaTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.yaml")
class TransformationJobRepositoryTest {
  @Autowired private TransformationJobRepository repository;

  @Test
  void testSaveAndRead() {
    UUID jobId = UUID.randomUUID();
    JpaTransactionJob job = new JpaTransactionJob();
    job.setId(jobId);
    job.setInput("Test input string");
    job.setCreatedAt(Instant.now());
    job.setIsJobCompletedSuccessfully(true);

    JpaTransformerTask task = new JpaTransformerTask();
    task.setId(1);
    task.setJob(job);
    task.setType(TransformerType.TO_UPPERCASE);
    task.setParameters("{ \"uppercase\": true }");

    job.getTransformers().add(task);

    repository.save(job);
    JpaTransactionJob loaded = repository.findById(jobId).orElseThrow();

    assertNotNull(loaded);
    assertEquals("Test input string", loaded.getInput());
    assertEquals(1, loaded.getTransformers().size());
    assertEquals(TransformerType.TO_UPPERCASE, loaded.getTransformers().get(0).getType());
  }


  @Test
  void findAllByCompletedAtBetween_shouldReturnAllInRange() {
    Instant from = Instant.parse("2023-01-01T00:00:00Z");
    Instant to = Instant.parse("2023-01-02T00:00:00Z");

    repository.save(createJobWithCompletedAt("Start", from));

    IntStream.of(6, 12, 18)
            .mapToObj(h -> from.plus(h, ChronoUnit.HOURS))
            .map(t -> createJobWithCompletedAt("Middle", t))
            .forEach(repository::save);

    repository.save(createJobWithCompletedAt("End", to.minusSeconds(1)));

    var results = repository.findAllByCompletedAtBetween(from, to, PageRequest.of(0, 10));

    assertEquals(5,results.getContent().size());
  }
  private JpaTransactionJob createJobWithCompletedAt(String inputPrefix, Instant createdAt) {
    JpaTransactionJob job = new JpaTransactionJob();
    job.setId(UUID.randomUUID());
    job.setInput(inputPrefix + " Job");
    job.setCreatedAt(createdAt);
    job.setCompletedAt(createdAt.plus(1, ChronoUnit.SECONDS));
    job.setIsJobCompletedSuccessfully(true);
    return job;
  }
}
