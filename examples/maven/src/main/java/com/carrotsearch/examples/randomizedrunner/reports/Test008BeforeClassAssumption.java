package com.carrotsearch.examples.randomizedrunner.reports;

import org.junit.*;

/** */
public class Test008BeforeClassAssumption {
  @BeforeAll
  public static void assumeMe() {
    Assumptions.assumeTrue(false);
  }

  @Test
  public void noop() {
  }
}
