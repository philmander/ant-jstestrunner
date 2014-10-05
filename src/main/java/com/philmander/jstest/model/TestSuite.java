package com.philmander.jstest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Meyer
 */
public class TestSuite {

    private Summary summary;

    private List<Test> tests = new ArrayList<Test>();

    private Error error;

    private String testFile;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public Error getError() {
        return error;
    }

    public void setErrors(Error errors) {
        this.error = error;
    }

    public String getTestFile() {
        return testFile;
    }

    public void setTestFile(String testFile) {
        this.testFile = testFile;
    }

    /**
     * @return Returns the number of tests that where successful.
     */
    public int getPassCount() {
        if (error != null) {
            // Test suite failed with an error
            return 0;
        }

        int result = 0;
        for (Test test : tests) {
            if (test.isSuccess()) {
                result++;
            }
        }

        return result;
    }

    /**
     * @return Returns the number of tests that have failed.
     */
    public int getFailCount() {
        if (error != null) {
            // Test suite failed with an error
            return 0;
        }

        int result = 0;
        for (Test test : tests) {
            if (test.hasFailed()) {
                result++;
            }
        }

        return result;
    }

    /**
     * @return Returns the number of test suites that failed without running any tests.
     */
    public int getErrorCount() {
        if (error != null) {
            // Test suite failed with an error
            return 1;
        }

        return 0;
    }

    /**
     * @return Returns the total number of tests.
     */
    public int getTotal() {
        if (error != null) {
            return 1;
        }

        return tests.size();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestSuite{");
        sb.append("summary=").append(summary);
        sb.append(", tests=").append(tests);
        sb.append(", error=").append(error);
        sb.append(", testFile='").append(testFile).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
