package com.carrotsearch.randomizedtesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.carrotsearch.randomizedtesting.WithNestedTestClass.FullResult;
import com.carrotsearch.randomizedtesting.annotations.Repeat;

/**
 * Try to be compatible with JUnit's runners wrt method hooks throwing
 * exceptions.
 */
public class TestHookMethodOrderWithExceptions extends RandomizedTest {
  static final List<String> callOrder = new ArrayList<String>();

  /**
   * Test superclass.
   */
  public abstract static class Super {
    static Random rnd;

    @BeforeAll
    public static void beforeClassSuper() {
      callOrder.add("beforeClassSuper");
      maybeThrowException();
    }

    @BeforeEach
    public final void beforeTest() {
      callOrder.add("beforeTestSuper");
      maybeThrowException();
    }

    @AfterEach
    public final void afterTest() {
      callOrder.add("afterTestSuper");
      maybeThrowException();

    }

    @AfterAll
    public static void afterClassSuper() {
      callOrder.add("afterClassSuper");
      maybeThrowException();
    }

    public static void maybeThrowException() {
      if (rnd != null && rnd.nextInt(10) == 0) {
        throw new RuntimeException();
      }
    }
  }

  /**
   * Test subclass.
   */
  public static class SubSub extends Super {
    @BeforeAll
    public static void beforeClass() {
      callOrder.add("beforeClassSub");
      maybeThrowException();
    }

    @BeforeEach
    public void beforeTestSub() {
      callOrder.add("beforeTestSub");
      maybeThrowException();
    }

    @Test
    public void testMethod() {
      callOrder.add("testMethodSub");
      maybeThrowException();
    }

    @AfterEach
    public void afterTestSub() {
      callOrder.add("afterTestSub");
      maybeThrowException();
    }

    @AfterAll
    public static void afterClass() {
      callOrder.add("afterClassSub");
      maybeThrowException();
    }
  }

  @BeforeEach
  public void setup() {
    callOrder.clear();
  }

  @AfterEach
  public void cleanup() {
    callOrder.clear();
  }

  @ExtendWith(RandomizedRunner.class)
  public static class WithRandomizedRunner extends SubSub {}
  public static class WithRegularRunner extends SubSub {}

  @Test @Repeat(iterations = 20)
  public void checkOrderSameAsJUnit() throws Exception {
    long seed = RandomizedContext.current().getRandomness().getSeed();

    callOrder.clear();
    Super.rnd = new Random(seed);

    FullResult r1 = WithNestedTestClass.runTests(WithRegularRunner.class);
    List<String> junitOrder = new ArrayList<String>(callOrder);

    callOrder.clear();
    Super.rnd = new Random(seed);
    FullResult r2 = WithNestedTestClass.runTests(WithRandomizedRunner.class);
    List<String> rrunnerOrder = new ArrayList<String>(callOrder);

    org.junit.jupiter.api.Assertions.assertEquals(junitOrder, rrunnerOrder);
    Assertions.assertThat(r1.getRunCount()).isEqualTo(r2.getRunCount());
  }
}
