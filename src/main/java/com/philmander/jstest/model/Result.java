package com.philmander.jstest.model;

/**
 * Model contains detailed information about the result of a <b>single</b> test.
 *
 * @author Michael Meyer
 */
public class Result {

    private int duration;

    /**
     * The number of failed assertions.
     */
    private int failed;

    private String name;

    /**
     * The number of passed assertions.
     */
    private int passed;

    /**
     * Execution time in milliseconds.
     */
    private int runtime;

    /**
     * The total number of assertions.
     */
    private int total;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getRuntimeInMilliseconds() {
        return runtime;
    }

    public double getRuntimeInSeconds() {
        return runtime / 1000.0;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Result{");
        sb.append("duration=").append(duration);
        sb.append(", failed=").append(failed);
        sb.append(", name='").append(name).append('\'');
        sb.append(", passed=").append(passed);
        sb.append(", runtime=").append(runtime);
        sb.append(", total=").append(total);
        sb.append('}');
        return sb.toString();
    }

}
