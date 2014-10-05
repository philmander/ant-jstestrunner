package com.philmander.jstest;

/**
 * @author Michael Meyer
 */
public class MessageUtil {

    public static String getSuccessMessage(int passCount) {
        String pluralPass = passCount > 1 ? "s" : "";
        return "JS tests passed. " + passCount + " test" + pluralPass + " passed.";
    }

    public static String getFailureMessage(int failCount, int errorCount) {
        String pluralFail = failCount > 1 ? "s" : "";
        String pluralErrors = errorCount > 1 ? "s" : "";
        return "JS tests failed. " + failCount + " test" + pluralFail + " failed. " + errorCount + " error" + pluralErrors + ".";
    }

}
