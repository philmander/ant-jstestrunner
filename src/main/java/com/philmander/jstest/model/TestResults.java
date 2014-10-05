package com.philmander.jstest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Meyer
 */
public class TestResults {

    private final List<TestSuite> testSuites = new ArrayList<TestSuite>();

    private int passCount = 0;

    private int failCount = 0;

    private int errorCount;

    public void addTestSuite(TestSuite testSuite) {
        testSuites.add(testSuite);

        passCount = passCount + testSuite.getPassCount();
        failCount = failCount + testSuite.getFailCount();
        errorCount = errorCount + testSuite.getErrorCount();
    }

    public List<TestSuite> getTestSuites() {
        return testSuites;
    }

    /**
     * @return Returns the number of tests that where successful.
     */
    public int getPassCount() {
        return passCount;
    }

    /**
     * @return Returns the number of tests that have failed.
     */
    public int getFailCount() {
        return failCount;
    }

    /**
     * @return Returns the number of test suites that failed without running any tests.
     */
    public int getErrorCount() {
        return errorCount;
    }

    public int getTotal() {
        return getPassCount() + getFailCount() + getErrorCount();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestResults{");
        sb.append("testSuites=").append(testSuites);
        sb.append(", passCount=").append(passCount);
        sb.append(", failCount=").append(failCount);
        sb.append(", errorCount=").append(errorCount);
        sb.append('}');
        return sb.toString();
    }

}
