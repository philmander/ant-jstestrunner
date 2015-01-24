package com.philmander.jstest.model;

/**
 * Model for a single assertion inside of a test.
 *
 * @author Michael Meyer
 */
public class Assertion {

    private String message;

    private String name;

    /**
     * If set to <code>true</code> the check was successful, <code>false</code> if the check failed.
     */
    private boolean result;

    private String source;

    private String expected;

    private String actual;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Assertion{");
        sb.append("message='").append(message).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", result=").append(result);
        sb.append(", source='").append(source).append('\'');
        sb.append(", expected='").append(expected).append('\'');
        sb.append(", actual='").append(actual).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
