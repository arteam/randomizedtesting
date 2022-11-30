package com.carrotsearch.randomizedtesting;

public abstract class Statement {
    /**
     * Run the action, throwing a {@code Throwable} if anything goes wrong.
     */
    public abstract void evaluate() throws Throwable;
}
