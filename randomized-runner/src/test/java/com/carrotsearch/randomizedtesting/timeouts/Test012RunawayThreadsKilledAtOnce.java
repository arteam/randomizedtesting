package com.carrotsearch.randomizedtesting.timeouts;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.*;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.carrotsearch.randomizedtesting.WithNestedTestClass;

@ExtendWith(RandomizedRunner.class)
public class Test012RunawayThreadsKilledAtOnce extends WithNestedTestClass {
  @ExtendWith(RandomizedRunner.class)
  public static class NestedClass {
    @Test
    public void lotsOfStubbornThreads() throws Throwable {
      assumeRunningNested();
      final CountDownLatch latch = new CountDownLatch(50);

      Thread [] threads = new Thread [(int) latch.getCount()];
      for (int i = 0; i < threads.length; i++) {
        threads[i] = new Thread("stubborn-" + i) {
          @Override
          public void run() {
            latch.countDown();

            try {
              Thread.sleep(20000);
            } catch (InterruptedException e) {
              // Ignore
            }
          }
        };
        threads[i].start();
      }

      // Wait for all threads to be really started.
      latch.await();
    }
  }

  @Test
  public void testLotsOfStubbornThreads() {
    long start = System.nanoTime();
    FullResult result = runTests(NestedClass.class);
    long end = System.nanoTime();

    Assertions.assertEquals(1, result.getFailureCount());
    long msec = TimeUnit.NANOSECONDS.toMillis(end - start);
    Assertions.assertTrue(msec + " msec?", msec < 1000 * 10);
  }
}
