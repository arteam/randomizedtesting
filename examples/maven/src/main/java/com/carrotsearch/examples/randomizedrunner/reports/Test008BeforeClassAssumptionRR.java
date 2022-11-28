package com.carrotsearch.examples.randomizedrunner.reports;

import org.junit.*;
import org.junit.jupiter.api.extension.ExtendWith;

import com.carrotsearch.randomizedtesting.RandomizedRunner;

/** */
@RunWith(RandomizedRunner.class)
public class Test008BeforeClassAssumptionRR {
  @BeforeAll
  public static void assumeMe() {
    Assumptions.assumeTrue(false);
  }

  @Test
  public void noop() {
  }
}
