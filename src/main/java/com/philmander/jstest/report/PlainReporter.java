package com.philmander.jstest.report;

import com.philmander.jstest.MessageUtil;
import com.philmander.jstest.model.*;
import com.philmander.jstest.model.Error;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Generate a plain text js test report
 *
 * @author Phil Mander
 */
public class PlainReporter implements JsTestResultReporter {

    private static final String DELIMITER =
            "===============================================================================\n";

    private static final String INDENT = "   ";

    public String createReport(TestResults results) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final BufferedWriter writer = new BufferedWriter(stringWriter);

        writer.write("Running JS Tests");
        writer.newLine();
        writer.write(DELIMITER);
        writer.newLine();


        for (TestSuite testSuite : results.getTestSuites()) {
            writer.write("Running file: " + testSuite.getTestFile());
            writer.newLine();

            if (testSuite.getError() == null) {
                int testIndex = 1;
                for (Test test : testSuite.getTests()) {
                    Result result = test.getResult();
                    writer.write(testIndex + ". " + test.getModule().getName() + ": " + result.getName()
                            + " (" + result.getPassed() + ", " + result.getFailed() + ", " + result.getTotal() + ")");
                    writer.newLine();

                    int assertionIndex = 1;
                    for (Assertion assertion : test.getAssertions()) {
                        if (assertion.isResult()) {
                            writer.write(INDENT + assertionIndex + ". Pass: " + assertion.getMessage());
                            writer.newLine();
                        } else {
                            writer.write(INDENT + assertionIndex + ". Fail: " + assertion.getMessage());
                            writer.newLine();
                            writer.write(INDENT + "Expected [" + assertion.getExpected() + "] but was [" + assertion.getActual() + "].");
                            writer.newLine();
                        }

                        assertionIndex++;
                    }

                    testIndex++;
                }

                Summary summary = testSuite.getSummary();
                writer.write("Tests completed in " + summary.getRuntime() + " milliseconds");
                writer.newLine();
                writer.write(testSuite.getPassCount() + " test of " + testSuite.getTotal() + " passed, " + testSuite.getFailCount() + " failed.");
                writer.newLine();
                writer.newLine();
            } else {
                Error error = testSuite.getError();
                writer.write("Error: " + error.getMessage());
                writer.newLine();
                writer.write(error.getTrace().toString());
                writer.newLine();
            }
        }

        writer.write(DELIMITER);
        writer.newLine();

        if (results.getFailCount() == 0 && results.getErrorCount() == 0) {
            writer.write(MessageUtil.getSuccessMessage(results.getPassCount()));
            writer.newLine();
        } else {
            writer.write(MessageUtil.getFailureMessage(results.getFailCount(), results.getErrorCount()));
            writer.newLine();
        }

        writer.flush();
        return stringWriter.toString();
    }

}
