package com.carrotsearch.randomizedtesting;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.notification.Failure;

import com.carrotsearch.randomizedtesting.annotations.Seed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link RandomizedRunner} can augment stack traces to include seed info. Check
 * if it works.
 */
public class TestStackAugmentation extends WithNestedTestClass {
  @ExtendWith(RandomizedRunner.class)
  @Seed("deadbeef")
  public static class Nested {
    @Test @Seed("cafebabe")
    public void testMethod1() {
      assumeRunningNested();

      // Throws a chained exception.
      try {
        throw new RuntimeException("Inner.");
      } catch (Exception e) {
        throw new Error("Outer.", e);
      }
    }
  }

  @Test
  public void testMethodLevel() {
    FullResult result = checkTestsOutput(1, 0, 1, 0, Nested.class);

    Failure f = result.getFailures().get(0);
    String seedFromThrowable = RandomizedRunner.seedFromThrowable(f.getException());
    assertNotNull(seedFromThrowable);
    assertTrue("[DEADBEEF:CAFEBABE]".compareToIgnoreCase(seedFromThrowable) == 0);
  }

  @ExtendWith(RandomizedRunner.class)
  @Seed("deadbeef")
  public static class Nested2 {
    @BeforeAll
    public static void beforeClass() {
      assumeRunningNested();
      throw new Error("beforeclass.");
    }

    @Test @Seed("cafebabe")
    public void testMethod1() {
    }
  }

  @Test
  public void testBeforeClass() {
    FullResult result = checkTestsOutput(0, 0, 1, 0, Nested2.class);
    assertEquals(1, result.getFailureCount());

    Failure f = result.getFailures().get(0);
    String seedFromThrowable = RandomizedRunner.seedFromThrowable(f.getException());
    assertNotNull(seedFromThrowable);
    assertTrue(f.getTrace(), "[DEADBEEF]".compareToIgnoreCase(seedFromThrowable) == 0);
  }

  @ExtendWith(RandomizedRunner.class)
  @Seed("deadbeef")
  public static class Nested3 {
    @AfterAll
    public static void afterClass() {
      assumeRunningNested();
      throw new Error("afterclass.");
    }

    @Test @Seed("cafebabe")
    public void testMethod1() {
    }
  }

  @Test
  public void testAfterClass() {
    FullResult result = checkTestsOutput(1, 0, 1, 0, Nested3.class);

    Failure f = result.getFailures().get(0);
    String seedFromThrowable = RandomizedRunner.seedFromThrowable(f.getException());
    assertNotNull(seedFromThrowable);
    assertTrue(f.getTrace(), "[DEADBEEF]".compareToIgnoreCase(seedFromThrowable) == 0);
  }  
}
