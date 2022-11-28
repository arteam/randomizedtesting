package com.carrotsearch.randomizedtesting.timeouts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.WithNestedTestClass;

/**
 * Test {@link Test#timeout()}.
 */
public class Test014Timeout extends WithNestedTestClass {
  public static class Nested extends RandomizedTest {
    @Test(timeout = 100)
    public void testMethod1() {
      assumeRunningNested();
      sleep(2000);
    }

    @Test(timeout = 100)
    public void testMethod2() {
      assumeRunningNested();
      while (!Thread.interrupted()) {
        // Do nothing.
      }
    }
  }

  @Test
  public void testTimeoutInTestAnnotation() {
    FullResult result = runTests(Nested.class);

    Assertions.assertEquals(0, result.getIgnoreCount());
    Assertions.assertEquals(2, result.getRunCount());
    Assertions.assertEquals(2, result.getFailureCount());
    Assertions.assertEquals(0, result.getAssumptionIgnored());
  }
}
