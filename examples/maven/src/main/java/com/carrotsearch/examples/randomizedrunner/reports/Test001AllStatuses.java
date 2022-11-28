package com.carrotsearch.examples.randomizedrunner.reports;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

public class Test001AllStatuses {
  @Test
  public void passed() {}
  
  @Test
  @Ignore
  public void ignored() {}

  @Test
  public void ignored_assumption() {
    Assumptions.assumeTrue(false);
  }

  @Test
  public void failure() {
    Assertions.fail();
  }

  @Test
  public void error() {
    throw new RuntimeException();
  }
}
