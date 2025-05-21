package org.gs.incode.services.stringtransformation.job;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Stream;
import org.gs.incode.services.stringtransformation.exceptions.StringTransformationException;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;
import org.gs.incode.services.stringtransformation.transformers.localeaware.UppercaseTransformerTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TransformationJobTest {
  @ParameterizedTest
  @MethodSource("transformationsSource")
  void whenTransformationListIsNotEmptyThanAppliesTransformations(
      String input, List<TransformerTask> tasks, String expected) {
    TransformationJob job = new TransformationJob(input, tasks);
    String result = job.execute();

    assertEquals(expected, result);
    assertSame(result, job.getResult());
    assertEquals(Status.COMPLETED, job.getStatus());
    assertTrue(job.isSuccess());
    assertNull(job.getError());
  }

  static Stream<Arguments> transformationsSource() {
    TransformerTask addWorldTask = input -> input + " world";
    return Stream.of(
        Arguments.of("hello", List.of(new UppercaseTransformerTask()), "HELLO"),
        Arguments.of("hello", List.of(new UppercaseTransformerTask(), addWorldTask), "HELLO world"),
        Arguments.of(
            "hello",
            List.of(new UppercaseTransformerTask(), addWorldTask, new UppercaseTransformerTask()),
            "HELLO WORLD"));
  }

  @ParameterizedTest
  @MethodSource("failedTransformationsSource")
  void whenOneOfTransformationTasksFailedThanJobIsFailed(
      List<TransformerTask> tasks, String expectedErrorMsg) {
    TransformationJob job = new TransformationJob("hello", tasks);
    assertThrows(StringTransformationException.class, job::execute);

    assertNull(job.getResult());
    assertEquals(Status.FAILED, job.getStatus());
    assertNotNull(job.getError());
    assertFalse(job.isSuccess());
    assertTrue(job.getError().contains(expectedErrorMsg));
  }

  static Stream<Arguments> failedTransformationsSource() {
    TransformerTask nullReturnTask = input -> null;
    return Stream.of(
        Arguments.of(List.of(new FailingTransformer()), "Test failure"),
        Arguments.of(
            List.of(new UppercaseTransformerTask(), new FailingTransformer()), "Test failure"),
        Arguments.of(
            List.of(new UppercaseTransformerTask(), nullReturnTask, new UppercaseTransformerTask()),
            "Transformation task #1 -"));
  }

  @Test
  void whenJobIsExecutedSecondTimeThanCachedResultReturns() {
    TransformationJob spyJob =
        spy(new TransformationJob("hello", List.of(new UppercaseTransformerTask())));
    String first = spyJob.execute();
    String second = spyJob.execute();

    assertSame(first, second);
    assertEquals("HELLO", second);
    verify(spyJob).applyTransformation();
  }

  @Test
  void whenValidConfigurationProvidedThenDoNotThrowException() {
    List<TransformerTask> tasks = List.of(mock(TransformerTask.class));
    assertDoesNotThrow(() -> new TransformationJob("input", tasks));
  }

  static class FailingTransformer implements TransformerTask {
    public String apply(String input) {
      throw new RuntimeException("Test failure");
    }
  }
}
