package com.carrotsearch.randomizedtesting;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestValidation extends WithNestedTestClass {
  public class SuiteClassNotStatic {
  }

  static class SuiteClassNotPublic {
  }

  public static class BeforeClassNotStatic {
    @BeforeAll
    public void beforeClass() {
    }
  }

  public static class BeforeClassWithArgs {
    @BeforeAll
    public static void beforeClass(int a) {
    }
  }

  public static class AfterClassNotStatic {
    @AfterAll
    public void afterClass() {
    }
  }

  public static class AfterClassWithArgs {
    @AfterAll
    public static void afterClass(int a) {
    }
  }

  public static class BeforeStatic {
    @BeforeEach
    public static void before() {
    }
  }

  public static class BeforeWithArgs {
    @BeforeEach
    public void before(int a) {
    }
  }

  public static class AfterStatic {
    @AfterEach
    public static void after() {
    }
  }

  public static class AfterWithArgs {
    @AfterEach
    public void after(int a) {
    }
  }

  @Test
  public void checkBeforeClass() throws Exception {
    for (Class<?> c : Arrays.asList(
        SuiteClassNotPublic.class, SuiteClassNotStatic.class,
        BeforeClassNotStatic.class, BeforeClassWithArgs.class,
        AfterClassNotStatic.class, AfterClassWithArgs.class,
        BeforeStatic.class, BeforeWithArgs.class,
        AfterStatic.class, AfterWithArgs.class)) {
      try {
        new RandomizedRunner(c);
        Assertions.fail("Expected validation failure on: " + c.getName());
      } catch (Exception e) {
        // Ok, expected.
      }
    }
  }
}
