package com.philmander.jstest.report;

import com.philmander.jstest.JsTestResults;
import com.philmander.jstest.MessageUtil;
import com.philmander.jstest.model.*;
import com.philmander.jstest.model.Error;
import org.apache.commons.lang.StringUtils;

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

    public String createReport(JsTestResults results) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final BufferedWriter writer = new BufferedWriter(stringWriter);

        writer.write("Running JS Tests");
        writer.newLine();
        writer.write(DELIMITER);
        writer.newLine();


        for (TestFile testFile : results.getTestFiles()) {
            writer.write("Running file: " + testFile.getFile());
            writer.newLine();

            if (testFile.getError() == null) {
                int testIndex = 1;
                for (Test test : testFile.getTests()) {
                    Result result = test.getResult();
                    writer.write(testIndex + ". " + test.getModule().getName() + ": " + result.getName()
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

                Summary summary = testFile.getSummary();
                writer.write("Tests completed in " + summary.getRuntime() + " milliseconds");
                writer.newLine();
                writer.write(testFile.getPassCount() + " test of " + testFile.getTotal() + " passed, " + testFile.getFailCount() + " failed.");
                writer.newLine();
                writer.newLine();
            } else {
                Error error = testFile.getError();
                writer.write("Error: " + error.getMessage());
                writer.newLine();
                writer.write(error.getFormattedTrace());
                writer.newLine();
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
