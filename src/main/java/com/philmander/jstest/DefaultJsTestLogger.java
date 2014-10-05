package com.philmander.jstest;

/**
 * @author Michael Meyer
 */
public class DefaultJsTestLogger implements JsTestLogger {

    public void log(String msg) {
        System.out.println("[jstest] " + msg);
    }

    public void error(String msg) {
        System.err.println("[jstest] " + msg);
    }

}
