package com.carrotsearch.randomizedtesting;

public class Failure {

    private final Description description;
    private final Throwable thrownException;

    public Failure(Description description, Throwable thrownException) {
        this.description = description;
        this.thrownException = thrownException;
    }

    public String getTestHeader() {
        return description.getDisplayName();
    }

    public Description getDescription() {
        return description;
    }

    public Throwable getException() {
        return thrownException;
    }

    @Override
    public String toString() {
        return getTestHeader() + ": " + thrownException.getMessage();
    }

    public String getMessage() {
        return getException().getMessage();
    }
}
