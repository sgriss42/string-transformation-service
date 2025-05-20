package org.gs.incode.services.stringtransformation.job;

import static org.gs.incode.services.stringtransformation.job.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.regex.Pattern;
import org.gs.incode.services.stringtransformation.transformers.UppercaseTransformerTask;
import org.gs.incode.services.stringtransformation.transformers.regexp.DeleteRegExpTransformer;
import org.gs.incode.services.stringtransformation.transformers.regexp.ReplaceExpTransformer;
import org.junit.jupiter.api.Test;

class BuilderTest {

  @Test
  void whenAddNullAsInputThanThrowsException() {
    Builder builder = new Builder();
    assertThrows(IllegalArgumentException.class, () -> builder.input(null));
  }

  @Test
  void whenAddNullAsTransformTaskThanThrowsException() {
    Builder builder = new Builder();
    assertThrows(IllegalArgumentException.class, () -> builder.addTransformerTask(null));
  }

  @Test
  void whenAllParametersProvidedThanReturnsTransformationJob() {
    DeleteRegExpTransformer deleteTask = DeleteRegExpTransformer.of(Pattern.compile(""));
    ReplaceExpTransformer replaceTask =
        ReplaceExpTransformer.of(Pattern.compile("pattern"), "replacement");
    UppercaseTransformerTask uppercaseTask1 = new UppercaseTransformerTask();
    UppercaseTransformerTask uppercaseTask2 = new UppercaseTransformerTask();
    TransformationJob job =
        new Builder()
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
}
