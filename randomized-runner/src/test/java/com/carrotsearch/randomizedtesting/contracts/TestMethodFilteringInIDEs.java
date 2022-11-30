package com.carrotsearch.randomizedtesting.contracts;

import org.junit.jupiter.api.Assumptions;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.Repeat;
import com.carrotsearch.randomizedtesting.annotations.Seed;

@Repeat(iterations = 2, useConstantSeed = false)
@Seed("deadbeef")
public class TestMethodFilteringInIDEs extends RandomizedTest {
  @Test
  public void testExecuted1() {
  }

  @Test
  public void testExecuted2() {
  }

  @Test
  public void testIgnoredByAssumption() {
    Assumptions.assumeTrue(false);
  }

  @Ignore
  @Test
  public void testIgnoredByAnnotation() {}
}
