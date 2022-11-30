package com.carrotsearch.randomizedtesting;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Description {

    private static final Pattern METHOD_AND_CLASS_NAME_PATTERN = Pattern
            .compile("([\\s\\S]*)\\((.*)\\)");

    public static Description createSuiteDescription(String name, Annotation... annotations) {
        return new Description(null, name, annotations);
    }

    public static Description createSuiteDescription(String name, Serializable uniqueId, Annotation... annotations) {
        return new Description(null, name, uniqueId, annotations);
    }

    public static Description createTestDescription(String className, String name, Annotation... annotations) {
        return new Description(null, formatDisplayName(name, className), annotations);
    }

    public static Description createTestDescription(Class<?> clazz, String name, Annotation... annotations) {
        return new Description(clazz, formatDisplayName(name, clazz.getName()), annotations);
    }

    public static Description createTestDescription(Class<?> clazz, String name) {
        return new Description(clazz, formatDisplayName(name, clazz.getName()));
    }

    public static Description createTestDescription(String className, String name, Serializable uniqueId) {
        return new Description(null, formatDisplayName(name, className), uniqueId);
    }

    private static String formatDisplayName(String name, String className) {
        return String.format("%s(%s)", name, className);
    }

    public static Description createSuiteDescription(Class<?> testClass) {
        return new Description(testClass, testClass.getName(), testClass.getAnnotations());
    }

    public static Description createSuiteDescription(Class<?> testClass, Annotation... annotations) {
        return new Description(testClass, testClass.getName(), annotations);
    }

    public static final Description EMPTY = new Description(null, "No Tests");

    public static final Description TEST_MECHANISM = new Description(null, "Test mechanism");

    private final Collection<Description> children = new ConcurrentLinkedQueue<Description>();
    private final String displayName;
    private final Serializable uniqueId;
    private final Annotation[] annotations;
    private volatile Class<?> testClass;

    private Description(Class<?> clazz, String displayName, Annotation... annotations) {
        this(clazz, displayName, displayName, annotations);
    }

    private Description(Class<?> testClass, String displayName, Serializable uniqueId, Annotation... annotations) {
        if ((displayName == null) || (displayName.length() == 0)) {
            throw new IllegalArgumentException(
                    "The display name must not be empty.");
        }
        if ((uniqueId == null)) {
            throw new IllegalArgumentException(
                    "The unique id must not be null.");
        }
        this.testClass = testClass;
        this.displayName = displayName;
        this.uniqueId = uniqueId;
        this.annotations = annotations;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void addChild(Description description) {
        children.add(description);
    }

    public List<Description> getChildren() {
        return new ArrayList<Description>(children);
    }

    public boolean isSuite() {
        return !isTest();
    }

    public boolean isTest() {
        return children.isEmpty();
    }

    public int testCount() {
        if (isTest()) {
            return 1;
        }
        int result = 0;
        for (Description child : children) {
            result += child.testCount();
        }
        return result;
    }

    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Description)) {
            return false;
        }
        Description d = (Description) obj;
        return uniqueId.equals(d.uniqueId);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public boolean isEmpty() {
        return equals(EMPTY);
    }

    public Description childlessCopy() {
        return new Description(testClass, displayName, annotations);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        for (Annotation each : annotations) {
            if (each.annotationType().equals(annotationType)) {
                return annotationType.cast(each);
            }
        }
        return null;
    }

    public Collection<Annotation> getAnnotations() {
        return Arrays.asList(annotations);
    }

    public Class<?> getTestClass() {
        if (testClass != null) {
            return testClass;
        }
        String name = getClassName();
        if (name == null) {
            return null;
        }
        try {
            testClass = Class.forName(name, false, getClass().getClassLoader());
            return testClass;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public String getClassName() {
        return testClass != null ? testClass.getName() : methodAndClassNamePatternGroupOrDefault(2, toString());
    }

    public String getMethodName() {
        return methodAndClassNamePatternGroupOrDefault(1, null);
    }

    private String methodAndClassNamePatternGroupOrDefault(int group,
                                                           String defaultString) {
        Matcher matcher = METHOD_AND_CLASS_NAME_PATTERN.matcher(toString());
        return matcher.matches() ? matcher.group(group) : defaultString;
    }
}
