package org.gs.incode.services.stringtransformation.job;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.stream.Stream;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;
import org.gs.incode.services.stringtransformation.transformers.UppercaseTransformerTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TransformationJobTest {
  @Test
  void whenNoTransformationTasksThenReturnsInput() {
    TransformationJob job = TransformationJob.builder().input("hello").build();
    String result = job.execute();

    assertSame("hello", result);
    assertNull(job.getError());
    assertSame(result, job.getResult());
    assertEquals(Status.COMPLETED, job.getStatus());
    assertTrue(job.isSuccess());
  }

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
    String result = job.execute();

    assertNull(result);
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
            "Transformation task #2 - [TO UPPERCASE TASK] failed:"));
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
  void whenInputIsNullThanThrowsException() {
    List<TransformerTask> transformationTasks = List.of(new UppercaseTransformerTask());

    assertThrows(
        IllegalArgumentException.class, () -> new TransformationJob(null, transformationTasks));
  }

  static class FailingTransformer implements TransformerTask {
    public String apply(String input) {
      throw new RuntimeException("Test failure");
    }
  }
}
