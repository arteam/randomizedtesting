package com.carrotsearch.examples.randomizedrunner;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.TestExecutionListener;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.Listeners;
import com.carrotsearch.randomizedtesting.listeners.ReproduceInfoPrinter;

/**
 * {@link RandomizedRunner} respects an on-suite class {@link Listeners}
 * annotation and instantiates classes that implement {@link TestExecutionListener}. This
 * allows custom listener hooks on the suite.
 * 
 * <p>
 * We honestly don't know where this would be useful. For now there are just a
 * few listeners, among them {@link ReproduceInfoPrinter} which dumps failure
 * information along with a preformatted JVM-options string to reproduce the
 * given test case (includes seed and filters).
 */
@Listeners({ReproduceInfoPrinter.class})
public class Test014Listeners extends RandomizedTest {
  @Test
  public void failure() {
    Assertions.assertTrue(false);
  }
}
