package com.carrotsearch.randomizedtesting;

import java.util.List;

public class Result {

    private final int count;
    private final int ignoreCount;
    private final int assumptionFailureCount;
    private final List<Failure> failures;
    private final long runTime;
    private final long startTime;

    public Result(int count, int ignoreCount, int assumptionFailureCount, List<Failure> failures, long runTime, long startTime) {
        this.count = count;
        this.ignoreCount = ignoreCount;
        this.assumptionFailureCount = assumptionFailureCount;
        this.failures = failures;
        this.runTime = runTime;
        this.startTime = startTime;
    }

    public int getCount() {
        return count;
    }

    public int getIgnoreCount() {
        return ignoreCount;
    }

    public int getAssumptionFailureCount() {
        return assumptionFailureCount;
    }

    public List<Failure> getFailures() {
        return failures;
    }

    public long getRunTime() {
        return runTime;
    }

    public long getStartTime() {
        return startTime;
    }
}
