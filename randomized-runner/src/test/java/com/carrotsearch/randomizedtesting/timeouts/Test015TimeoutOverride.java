package com.carrotsearch.randomizedtesting.timeouts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.SysGlobals;
import com.carrotsearch.randomizedtesting.WithNestedTestClass;
import com.carrotsearch.randomizedtesting.annotations.Timeout;

import java.util.concurrent.TimeUnit;


/**
 * Test global timeout override (-Dtests.timeout=1000!).
 */
public class Test015TimeoutOverride extends WithNestedTestClass {
  public static class Nested extends RandomizedTest {
    @Test
    @Timeout(millis = 5000)
    public void testMethod1() {
      assumeRunningNested();
      sleep(10000);
    }
  }

  public static class Nested2 extends RandomizedTest {
    @Test
    @Timeout(millis = 100)
    public void testMethod1() {
      assumeRunningNested();
      sleep(1000);
    }
  }

  @Test
  public void testTimeoutOverride() {
    System.setProperty(SysGlobals.SYSPROP_TIMEOUT(), "200!");
    long start = System.nanoTime();
    FullResult result = runTests(Nested.class);
    long end = System.nanoTime();
    Assertions.assertEquals(1, result.getFailureCount());
    Assertions.assertTrue(TimeUnit.NANOSECONDS.toMillis(end - start) < 3000);
  }
  
  @Test
  public void testDisableTimeout() {
    System.setProperty(SysGlobals.SYSPROP_TIMEOUT(), "0!");

    long start = System.nanoTime();
    FullResult result = runTests(Nested2.class);
    long end = System.nanoTime();
    Assertions.assertEquals(0, result.getFailureCount());
    Assertions.assertTrue(TimeUnit.NANOSECONDS.toMillis(end - start) > 900);
  }
  
  @AfterEach
  public void cleanup() {
    System.clearProperty(SysGlobals.SYSPROP_TIMEOUT());
  }
}
