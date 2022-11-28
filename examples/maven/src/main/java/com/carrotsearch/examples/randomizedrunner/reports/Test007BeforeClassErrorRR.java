package com.carrotsearch.examples.randomizedrunner.reports;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.carrotsearch.randomizedtesting.RandomizedRunner;

/** */
@RunWith(RandomizedRunner.class)
public class Test007BeforeClassErrorRR {
  @BeforeAll
  public static void errorOnMe() {
    throw new RuntimeException();
  }

  @Test
  public void noop() {
  }
}
