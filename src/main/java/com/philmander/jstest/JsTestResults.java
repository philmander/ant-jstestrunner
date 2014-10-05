package com.philmander.jstest;

import com.philmander.jstest.model.TestFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Meyer
 */
public class JsTestResults {

    private final List<TestFile> testFiles = new ArrayList<TestFile>();

    private int passCount = 0;

    private int failCount = 0;

    private int errorCount;

    public void addTestFile(TestFile testFile) {
        testFiles.add(testFile);

        passCount = passCount + testFile.getPassCount();
        failCount = failCount + testFile.getFailCount();
        errorCount = errorCount + testFile.getErrorCount();
    }

    public List<TestFile> getTestFiles() {
        return testFiles;
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
        final StringBuilder sb = new StringBuilder("JsTestResults{");
        sb.append("testFiles=").append(testFiles);
        sb.append(", passCount=").append(passCount);
        sb.append(", failCount=").append(failCount);
        sb.append(", errorCount=").append(errorCount);
        sb.append('}');
        return sb.toString();
    }

}
