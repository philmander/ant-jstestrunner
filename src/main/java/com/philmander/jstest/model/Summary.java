package com.philmander.jstest.model;

/**
 * Contains information about all tests in a test suite.
 * <p>For failed, passed and total the granularity is Assertion and not Test.</p>
 *
 * @author Michael Meyer
 */
public class Summary {

    private int failed;

    private int passed;

    private int total;

    private int runtime;

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getRuntimeInSeconds() {
        return runtime / 1000.0;
    }

    public int getRuntimeInMilliseconds() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Summary{");
        sb.append("failed=").append(failed);
        sb.append(", passed=").append(passed);
        sb.append(", total=").append(total);
        sb.append(", runtime=").append(runtime);
        sb.append('}');
        return sb.toString();
    }

}
