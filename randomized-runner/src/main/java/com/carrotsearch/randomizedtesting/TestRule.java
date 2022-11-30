package com.carrotsearch.randomizedtesting;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;

public interface TestRule extends BeforeEachCallback, AfterEachCallback {
}
