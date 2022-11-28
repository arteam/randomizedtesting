package com.carrotsearch.examples.randomizedrunner.reports;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.carrotsearch.randomizedtesting.RandomizedRunner;

/** */
@RunWith(RandomizedRunner.class)
public class Test006BeforeClassFailureRR {
  @BeforeAll
  public static void failOnMe() {
    Assertions.assertTrue(false);
  }

  @Test
  public void noop() {
  }
}
