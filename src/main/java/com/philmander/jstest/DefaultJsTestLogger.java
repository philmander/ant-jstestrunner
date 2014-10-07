package com.philmander.jstest;

/**
 * @author Michael Meyer
 */
public class DefaultJsTestLogger implements JsTestLogger {

    public void log(String message) {
        System.out.println(message);
    }

    public void error(String message) {
        System.err.println(message);
    }

}
