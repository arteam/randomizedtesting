package com.carrotsearch.examples.randomizedrunner.reports;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** */
public class Test006BeforeClassFailure {
  @BeforeAll
  public static void failOnMe() {
    Assertions.assertTrue(false);
  }

  @Test
  public void noop() {
  }
}
