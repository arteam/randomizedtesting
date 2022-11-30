package com.carrotsearch.randomizedtesting;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.carrotsearch.randomizedtesting.annotations.Repeat;

/**
 * Nightly mode checks.
 */
public class TestIterationsAnnotation extends RandomizedTest {
  static int iterations = 0;

  @Test @Repeat(iterations = 10)
  public void nightly() {
    iterations++;
  }

  @BeforeAll
  public static void clean() {
    iterations = 0;
  }
  
  @AfterAll
  public static void cleanupAfter() {
    Assertions.assertEquals(10, iterations);
  }
}
