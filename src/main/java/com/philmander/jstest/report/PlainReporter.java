package com.philmander.jstest.report;

import com.philmander.jstest.JsTestResults;
import com.philmander.jstest.MessageUtil;
import com.philmander.jstest.model.TestFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Generate a plain text js test report
 *
 * @author Phil Mander
 */
public class PlainReporter implements JsTestResultReporter {

    public static final String DELIMITER =
            "===============================================================================";

    public String createReport(JsTestResults results) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final BufferedWriter writer = new BufferedWriter(stringWriter);

        writer.write("JS Test Results");
        writer.newLine();
        writer.write(DELIMITER);
        writer.newLine();
        writer.newLine();


        for (TestFile testFile : results.getTestFiles()) {
            writer.write("Running file: " + testFile.getFile() + "...");
            writer.newLine();

            writer.write(MessageUtil.getTestFileReport(testFile));
        }

        writer.write(DELIMITER);
        writer.newLine();
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
