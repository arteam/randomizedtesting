package com.carrotsearch.randomizedtesting;

import org.junit.jupiter.api.Test;

import com.carrotsearch.randomizedtesting.annotations.Timeout;

import static org.junit.Assertions.*;

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
  @Timeout(millis = 0)
  public void testMarkerXYZ() {
    String tName = Thread.currentThread().getName();
    assertTrue(tName, tName.contains("testMarkerXYZ"));
  }

  @Test
  @Timeout(millis = 1000)
  public void testMarkerKJI() {
    String tName = Thread.currentThread().getName();
    assertTrue(tName, tName.contains("testMarkerKJI"));
  }    
}
