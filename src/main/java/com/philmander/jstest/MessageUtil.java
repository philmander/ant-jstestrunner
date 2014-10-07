package com.philmander.jstest;

import com.philmander.jstest.model.*;
import com.philmander.jstest.model.Error;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Michael Meyer
 */
public final class MessageUtil {

    public static final String INDENT = "   ";

    private MessageUtil() {
        // utility class
    }

    public static String getSuccessMessage(int passCount) {
        String pluralPass = passCount > 1 ? "s" : "";
        return "JS tests passed. " + passCount + " test" + pluralPass + " passed.";
    }

    public static String getFailureMessage(int failCount, int errorCount) {
        String pluralFail = failCount > 1 ? "s" : "";
        String pluralErrors = errorCount > 1 ? "s" : "";
        return "JS tests failed. " + failCount + " test" + pluralFail + " failed. " + errorCount + " error" + pluralErrors + ".";
    }

    public static String getTestFileReport(TestFile testFile) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final BufferedWriter writer = new BufferedWriter(stringWriter);

        if (testFile.getError() == null) {
            int testIndex = 1;
            for (Test test : testFile.getTests()) {
                final Result result = test.getResult();

                final String module = StringUtils.isNotBlank(test.getModule().getName())
                        ? test.getModule().getName() + ": "
                        : StringUtils.EMPTY;

                writer.write(testIndex + ". " + module + result.getName()
                        + " (" + result.getPassed() + ", " + result.getFailed() + ", " + result.getTotal() + ")");
                writer.newLine();

                int assertionIndex = 1;
                for (Assertion assertion : test.getAssertions()) {
                    if (assertion.isResult()) {
                        writer.write(INDENT + assertionIndex + ". Pass");
                        if (StringUtils.isNotBlank(assertion.getMessage())) {
                            writer.write(": " + assertion.getMessage());
                        }
                        writer.newLine();
                    } else {
                        writer.write(INDENT + assertionIndex + ". Fail");
                        if (StringUtils.isNotBlank(assertion.getMessage())) {
                            writer.write(": " + assertion.getMessage());
                        }
                        writer.newLine();
                        writer.write(INDENT + "Expected [" + assertion.getExpected() + "] but was [" + assertion.getActual() + "].");
                        writer.newLine();
                    }

                    assertionIndex++;
                }

                testIndex++;
            }

            final Summary summary = testFile.getSummary();
            writer.write("Tests completed in " + summary.getRuntimeInMilliseconds() + " milliseconds");
            writer.newLine();
            writer.write(testFile.getPassCount() + " test of " + testFile.getTotal() + " passed, " + testFile.getFailCount() + " failed.");
            writer.newLine();
            writer.newLine();
        } else {
            final Error error = testFile.getError();
            writer.write("Error: " + error.getMessage());
            writer.newLine();
            writer.write(error.getFormattedTrace());
            writer.newLine();
            writer.newLine();
        }

        writer.flush();
        return stringWriter.toString();
    }

}
