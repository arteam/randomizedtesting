package com.carrotsearch.ant.tasks.junit4.tests;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.junit.BeforeClass;
import org.junit.Test;

public class SysoutsOnSuiteTimeout extends RandomizedTest {
  @BeforeClass
  public static void beforeClass() {
    System.out.println("beforeclass-sysout.");
  }

  @Test(timeout = 1000)
  public void success() throws Exception {
    System.out.println("test-sysout.");
    Thread.sleep(100000);
  }
}
