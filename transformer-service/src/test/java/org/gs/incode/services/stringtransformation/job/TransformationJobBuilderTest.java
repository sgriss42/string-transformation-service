package org.gs.incode.services.stringtransformation.job;

import static org.gs.incode.services.stringtransformation.job.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.regex.Pattern;
import org.gs.incode.services.stringtransformation.exceptions.InitTransformationServiceException;
import org.gs.incode.services.stringtransformation.exceptions.TransformationServiceException;
import org.gs.incode.services.stringtransformation.transformers.TransformerTask;
import org.gs.incode.services.stringtransformation.transformers.localeaware.UppercaseTransformerTask;
import org.gs.incode.services.stringtransformation.transformers.regexp.DeleteRegExpTransformer;
import org.gs.incode.services.stringtransformation.transformers.regexp.ReplaceExpTransformer;
import org.junit.jupiter.api.Test;

class TransformationJobBuilderTest {

  @Test
  void whenAddNullAsInputThanThrowsException() {
    TransformationJobBuilder transformationJobBuilder = new TransformationJobBuilder();
    assertThrows(
        InitTransformationServiceException.class, () -> transformationJobBuilder.input(null));
  }

  @Test
  void whenAddNullAsTransformTaskThanThrowsException() {
    TransformationJobBuilder transformationJobBuilder = new TransformationJobBuilder();
    assertThrows(
        InitTransformationServiceException.class,
        () -> transformationJobBuilder.addTransformerTask(null));
  }

  @Test
  void whenAllParametersProvidedThanReturnsTransformationJob() {
    DeleteRegExpTransformer deleteTask = DeleteRegExpTransformer.of(Pattern.compile(""));
    ReplaceExpTransformer replaceTask =
        ReplaceExpTransformer.of(Pattern.compile("pattern"), "replacement");
    UppercaseTransformerTask uppercaseTask1 = new UppercaseTransformerTask();
    UppercaseTransformerTask uppercaseTask2 = new UppercaseTransformerTask();
    TransformationJob job =
        new TransformationJobBuilder()
            .addTransformerTask(uppercaseTask1)
            .addTransformerTask(deleteTask)
            .addTransformerTask(uppercaseTask2)
            .addTransformerTask(replaceTask)
            .input("input")
            .build();

    assertNotNull(job);
    assertEquals(NEW, job.getStatus());
    assertNull(null, job.getError());
    assertNull(null, job.getResult());
    assertEquals("input", job.getInput());
    assertIterableEquals(
        List.of(uppercaseTask1, deleteTask, uppercaseTask2, replaceTask),
        job.getTransformerTasks());
  }

  @Test
  void whenTransformerTaskListExceedsLimitThenThrowTransformationServiceException() {
    TransformationJobBuilder transformationJobBuilder =
        new TransformationJobBuilder().input("input");
    for (int i = 0; i < TransformationJobBuilder.MAX_TRANSFORMER + 1; i++) {
      transformationJobBuilder.addTransformerTask(mock(TransformerTask.class));
    }

    TransformationServiceException ex =
        assertThrows(TransformationServiceException.class, transformationJobBuilder::build);
    assertTrue(ex.getMessage().contains("Too many Transformer Tasks"));
  }

  @Test
  void whenNoTransformationTasksThenThrowException() {
    TransformationJobBuilder transformationJobBuilder =
        new TransformationJobBuilder().input("input");

    assertThrows(TransformationServiceException.class, transformationJobBuilder::build);
  }
}
