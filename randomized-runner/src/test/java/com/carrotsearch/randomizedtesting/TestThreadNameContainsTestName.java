package com.carrotsearch.randomizedtesting;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test {@link Test#expected()}.
 */
public class TestThreadNameContainsTestName extends RandomizedTest {
  @Test
  public void testMarkerABC() {
    String tName = Thread.currentThread().getName();
    assertTrue(tName, tName.contains("testMarkerABC"));
  }

  @Test
  public void testMarkerXYZ() {
    String tName = Thread.currentThread().getName();
    assertTrue(tName, tName.contains("testMarkerXYZ"));
  }

  @Test
  public void testMarkerKJI() {
    String tName = Thread.currentThread().getName();
    assertTrue(tName, tName.contains("testMarkerKJI"));
  }    
}
