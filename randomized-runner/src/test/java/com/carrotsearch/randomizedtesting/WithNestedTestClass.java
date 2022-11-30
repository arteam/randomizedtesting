package com.carrotsearch.randomizedtesting;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.TestExecutionListener;
import org.junit.runners.model.Statement;

import com.carrotsearch.randomizedtesting.rules.StatementAdapter;
import com.carrotsearch.randomizedtesting.rules.SystemPropertiesInvariantRule;
import com.carrotsearch.randomizedtesting.rules.TestRuleAdapter;

/**
 * Utility class to surround nested {@link RandomizedRunner} test suites.
 */
public class WithNestedTestClass {
  private static boolean runningNested;

  public enum Place {
    CLASS_RULE,
    BEFORE_CLASS,
    CONSTRUCTOR,
    TEST_RULE,
    BEFORE,
    TEST,
    AFTER,
    AFTER_CLASS,
  }

  public static class ApplyAtPlace extends RandomizedTest {
    public static Place place;
    public static Runnable runnable;

    @ClassRule
    public static TestRule classRule = new TestRuleAdapter() {
      protected void before() throws Throwable {
        ApplyAtPlace.apply(Place.CLASS_RULE);
      }
    };

    @BeforeAll 
    public static void beforeClass() { apply(Place.BEFORE_CLASS); }

    public ApplyAtPlace() { apply(Place.CONSTRUCTOR); }

    @Rule
    public TestRule testRule = new TestRuleAdapter() {
      protected void before() throws Throwable {
        ApplyAtPlace.apply(Place.TEST_RULE);
      }
    };

    @BeforeEach 
    public void before() { apply(Place.BEFORE); }

    @Test
    public void testMethod() { apply(Place.TEST); }

    @AfterEach
    public void after() { apply(Place.AFTER); }

    @AfterAll 
    public static void afterClass() { apply(Place.AFTER_CLASS); }

    private static void apply(Place p) {
      if (place == p) {
        assumeRunningNested();
        runnable.run();
      }
    }
  }

  static {
    TestRule dumpLoggerOutputOnFailure = new TestRule() {
      @Override
      public Statement apply(Statement base, final Description description) {
        return new StatementAdapter(base) {
          protected void afterAlways(java.util.List<Throwable> errors) throws Throwable {
            if (!errors.isEmpty()) {
              sysout.println("-- " + description);
              sysout.println(loggingMessages);
            }
          }
        };
      }
    };
    
    SystemPropertiesInvariantRule noLeftOverProperties =
        new SystemPropertiesInvariantRule(new HashSet<String>(Arrays.asList(
            "user.timezone")));
    
    ruleChain = RuleChain
      .outerRule(noLeftOverProperties)
      .around(dumpLoggerOutputOnFailure);
  }

  @ClassRule
  public final static TestRule ruleChain;

  /** For capturing sysout. */
  protected static PrintStream sysout;

  /** For capturing syserr. */
  protected static PrintStream syserr;

  /**
   * Captured sysout/ syserr.
   */
  private static StringWriter sw;
  
  /**
   * Captured java logging messages. 
   */
  private static StringWriter loggingMessages;

  /**
   * Main logger.
   */
  private static Logger logger;

  /**
   * Previous handlers..
   */
  private static Handler[] handlers;

  /**
   * Zombie threads.
   */
  private static List<Thread> zombies = new ArrayList<>();

  private static volatile Object zombieToken;

  @BeforeAll
  public static final void setupNested() throws IOException {
    runningNested = true;
    zombieToken = new Object();

    // capture sysout/ syserr.
    sw = new StringWriter();
    sysout = System.out;
    syserr = System.err;
    System.setOut(new PrintStream(new TeeOutputStream(System.out, new WriterOutputStream(sw))));
    System.setErr(new PrintStream(new TeeOutputStream(System.err, new WriterOutputStream(sw))));
    
    // Add custom logging handler because java logging keeps a reference to previous System.err.
    loggingMessages = new StringWriter();
    logger = Logger.getLogger("");
    handlers = logger.getHandlers();
    for (Handler h : handlers) logger.removeHandler(h);
    logger.addHandler(new Handler() {
        final SimpleFormatter formatter = new SimpleFormatter();

        @Override
        public void publish(LogRecord record) {
          loggingMessages.write(formatter.format(record) + "\n");
        }

        @Override
        public void flush() {}
        
        @Override
        public void close() throws SecurityException {}
      });
  }

  @AfterAll
  public static final void clearNested() throws Exception {
    zombieToken = null;
    runningNested = false;
    ApplyAtPlace.runnable = null;
    ApplyAtPlace.place = null;
    
    System.setOut(sysout);
    System.setErr(syserr);

    for (Handler h : logger.getHandlers()) logger.removeHandler(h);
    for (Handler h : handlers) logger.addHandler(h);
    
    for (Thread t : zombies) {
      t.interrupt();
    }
    for (Thread t : zombies) {
      t.join();
    }    
  }

  @AfterEach
  public void after() {
    // Reset zombie thread marker.
    RandomizedRunner.zombieMarker.set(false);
  }
  
  @BeforeEach
  public void before() {
    sw.getBuffer().setLength(0);
    loggingMessages.getBuffer().setLength(0);
  }
  
  protected static String getSysouts() {
    System.out.flush();
    System.err.flush();
    return sw.toString();
  }

  public static String getLoggingMessages() {
    return loggingMessages.toString();
  }
  
  protected static boolean isRunningNested() {
    return runningNested;
  }
  
  protected static void assumeRunningNested() {
    Assumptions.assumeTrue(runningNested);
  }
  
  protected static Thread startZombieThread(String name) {
    final CountDownLatch latch = new CountDownLatch(1);
    Thread t = new Thread(name) {
      private final Object token = zombieToken; 

      public void run() {
        latch.countDown();
        while (zombieToken == token) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            // ignore.
          }
        }
      }
    };
    t.start();
    zombies.add(t);
    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return t;
  }
  
  protected static Thread startThread(String name) {
    final CountDownLatch latch = new CountDownLatch(1);
    Thread t = new Thread(name) {
      public void run() {
        latch.countDown();
        sleepForever();
      }

      private void sleepForever() {
        while (true) RandomizedTest.sleep(1000);
      }
    };
    t.start();
    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return t;
  }

  public static class FullResult {
    private AtomicInteger assumptionIgnored = new AtomicInteger();
    private Result result;
    
    public int getRunCount() {
      return result.getRunCount();
    }

    public int getIgnoreCount() {
      return result.getIgnoreCount();
    }
    
    public int getFailureCount() {
      return result.getFailureCount();
    }

    public int getAssumptionIgnored() {
      return assumptionIgnored.get();
    }

    public List<Failure> getFailures() {
      return result.getFailures();
    }

    public boolean wasSuccessful() {
      return result.wasSuccessful();
    }
  }
  
  public static FullResult runTests(final Class<?>... classes) {
    try {
      final FullResult fullResult = new FullResult();
      
      // Run on a separate thread so that it appears as we're not running in an IDE. 
      Thread thread = new Thread() {
        @Override
        public void run() {
          LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                  .selectors(Arrays.stream(classes).map(DiscoverySelectors::selectClass).collect(Collectors.toList()))
                  .build();
          Launcher launcher = LauncherFactory.create();
          launcher.discover(request);
          launcher.registerTestExecutionListeners(new PrintEventListener(sysout));
          launcher.registerTestExecutionListeners(new TestExecutionListener() {
            @Override
            public void testAssumptionFailure(Failure failure) {
              fullResult.assumptionIgnored.incrementAndGet();
            }
          });

          fullResult.result = core.run(classes);
        }
      };

      thread.start();
      thread.join();
      return fullResult;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static FullResult checkTestsOutput(int run, int ignored, int failures, int assumptions, Class<?> classes) {
    FullResult result = runTests(classes);
    if (result.getRunCount() != run ||
        result.getIgnoreCount() != ignored ||
        result.getFailureCount() != failures ||
        result.getAssumptionIgnored() != assumptions) {
      Assertions.fail("Different result. [run,ign,fail,ass] Expected: "
          + run + "," + ignored + "," + failures + "," + assumptions + 
          ", Actual: " + result.getRunCount() + "," + result.getIgnoreCount() + "," + result.getFailureCount()
          + "," + result.getAssumptionIgnored());
    }
    return result;
  }
}
