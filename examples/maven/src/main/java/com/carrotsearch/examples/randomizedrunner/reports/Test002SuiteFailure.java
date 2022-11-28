package com.carrotsearch.examples.randomizedrunner.reports;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Suite-level failures.
 */
public class Test002SuiteFailure {
  @BeforeAll
  public static void beforeClass() {
    throw new RuntimeException("beforeClass");
  }

  @Test
  public void testCase() {}

  @AfterAll
  public static void afterClass() {
    throw new RuntimeException("afterClass");
  }
}
