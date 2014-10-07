package com.philmander.jstest.model;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author Michael Meyer
 */
public class Error {

    private static final String NEWLINE = System.getProperty("line.separator");

    private static final String FILE_KEY = "file";

    private static final String LINE_KEY = "line";

    private static final String FUNCTION_KEY = "function";

    private String message;

    private Map<String, String>[] trace;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String>[] getTrace() {
        return trace;
    }

    public String getFormattedTrace() {
        final StringBuilder sb = new StringBuilder();

        if (trace != null) {
            boolean first = true;
            for (Map<String, String> map : trace) {
                final String line = getFormattedTraceLine(map);
                if (StringUtils.isNotBlank(line)) {
                    if (!first) {
                        sb.append(NEWLINE);
                    }
                    sb.append(line);

                    first = false;
                }
            }
        }

        return sb.toString();
    }

    private String getFormattedTraceLine(Map<String, String> map) {
        final String file = map.get(FILE_KEY);
        final String line = map.get(LINE_KEY);
        final String function = map.get(FUNCTION_KEY);

        final StringBuilder sb = new StringBuilder();
        boolean first = true;

        if (StringUtils.isNotBlank(function)) {
            sb.append("function: ").append(function);
            first = false;
        }

        if (StringUtils.isNotBlank(line)) {
            if (!first) {
                sb.append(", ");
            }
            sb.append("line: ").append(line);
            first = false;
        }

        if (StringUtils.isNotBlank(file)) {
            if (!first) {
                sb.append(", ");
            }
            sb.append("file: ").append(file);
        }

        return sb.toString();
    }

    public void setTrace(Map<String, String>[] trace) {
        this.trace = trace;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Error{");
        sb.append("message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
