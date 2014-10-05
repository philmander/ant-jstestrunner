package com.philmander.jstest.model;

import java.util.Arrays;

/**
 * @author Michael Meyer
 */
public class Error {

    private String message;

    private Object[] trace;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getTrace() {
        return trace;
    }

    public void setTrace(Object[] trace) {
        this.trace = trace;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Error{");
        sb.append("message='").append(message).append('\'');
        sb.append(", trace=").append(Arrays.toString(trace));
        sb.append('}');
        return sb.toString();
    }
    
}
