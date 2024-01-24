package com.carrotsearch.ant.tasks.junit4.tests;

import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.TimeoutSuite;

@TimeoutSuite(millis = 1000)
public class SysoutsOnSuiteTimeout extends RandomizedTest {
  @BeforeClass
  public static void beforeClass() {
    System.out.println("beforeclass-sysout.");
  }

  @Test
  public void success() throws Exception {
    System.out.println("test-sysout.");
    Thread.sleep(100000);
  }  
}
