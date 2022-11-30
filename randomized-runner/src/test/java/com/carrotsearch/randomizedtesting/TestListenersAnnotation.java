package com.carrotsearch.randomizedtesting;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.TestExecutionListener;

import com.carrotsearch.randomizedtesting.annotations.Listeners;

/**
 * Test listeners on suite.
 */
public class TestListenersAnnotation extends WithNestedTestClass {
  
  public static List<String> buffer = new ArrayList<String>();

  public static class NoopListener extends TestExecutionListener {
  }

  public static class BufferAppendListener extends TestExecutionListener {
    public void testRunStarted(Description description) throws Exception {
      buffer.add("run started: " + description.getMethodName());
    }
    
    public void testStarted(Description description) throws Exception {
      buffer.add("test started: " + description.getMethodName());
    }
    
    @Override
    public void testFinished(Description description) throws Exception {
      buffer.add("test finished: " + description.getMethodName());
    }
    
    @Override
    public void testAssumptionFailure(Failure failure) {
      buffer.add("assumption failed: " + failure.getDescription().getMethodName());
    }

    @Override
    public void testIgnored(Description description) throws Exception {
      buffer.add("test ignored: " + description.getMethodName());
    }
    
    @Override
    public void testFailure(Failure failure) throws Exception {
      buffer.add("failure: " + failure.getDescription().getMethodName());
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
      buffer.add("run finished: " + result.getRunCount());
    }
  }

  @Listeners({BufferAppendListener.class})
  public static class Nested1 extends RandomizedTest {
  }

  @Listeners({NoopListener.class})
  public static class Nested2 extends Nested1 {
    @Test @Ignore
    public void ignored() {
    }

    @Test
    public void passing() throws Exception {
    }
    
    @Test
    public void failing() throws Exception {
      assumeRunningNested();
      Assertions.fail();
    }

    @Test
    public void assumptionFailing() throws Exception {
      assumeRunningNested();
      Assumptions.assumeTrue(false);
    }
  }

  @Test
  public void checkListeners() {
    runTests(Nested2.class);
    // Perhaps this is overly simple, but we just want to know that it executed.
    Assertions.assertTrue(buffer.size() > 0);
  }
}
