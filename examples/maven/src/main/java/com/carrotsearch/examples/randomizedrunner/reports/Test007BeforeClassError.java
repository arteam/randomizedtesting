package com.carrotsearch.examples.randomizedrunner.reports;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** */
public class Test007BeforeClassError {
  @BeforeAll
  public static void errorOnMe() {
    throw new RuntimeException();
  }

  @Test
  public void noop() {
  }
}
