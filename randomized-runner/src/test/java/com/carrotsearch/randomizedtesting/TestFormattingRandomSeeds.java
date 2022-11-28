package com.carrotsearch.randomizedtesting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.carrotsearch.randomizedtesting.annotations.Repeat;

@RunWith(RandomizedRunner.class)
public class TestFormattingRandomSeeds {
  @Test
  public void minusOne() {
    check(-1L);
  }
  
  @Test
  public void zero() {
    check(0);
  }
  
  @Test
  public void maxLong() {
    check(Long.MAX_VALUE);
  }

  /** Heck, why not use ourselves here? ;) */
  @Test
  @Repeat(iterations = 1000)
  public void noise() {
    check(RandomizedContext.current().getRandom().nextLong());
  }

  private void check(long seed) {
    String asString = SeedUtils.formatSeedChain(new Randomness(seed, RandomSupplier.DEFAULT));
    Assertions.assertEquals(seed, SeedUtils.parseSeedChain(asString)[0]);
  }
}
