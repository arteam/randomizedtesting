package com.carrotsearch.examples.randomizedrunner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.carrotsearch.randomizedtesting.RandomizedRunner;

/**
 * This is a test-based tutorial introducing to randomized JUnit testing using
 * {@link RandomizedRunner}. Follow test cases in their alphabetic order. 
 * 
 * <p>One way to start using {@link RandomizedRunner} is to declare
 * your suite class as being executed by {@link RandomizedRunner} (using
 * {@link RunWith} annotation). The {@link #success()} method doesn't do anything
 * useful but runs under {@link RandomizedRunner}. We know this for sure because we
 * can hide a hook (<code>before()</code>) method to be private 
 * (normal JUnit doesn't allow this). 
 */
@ExtendWith(RandomizedRunner.class)
public class Test001SimpleUseCase {
  @BeforeEach
  private void before() {
    // This won't work under the default JUnit runner.
  }

  @Test
  public void success() {
    // Do nothing.
  }
}
