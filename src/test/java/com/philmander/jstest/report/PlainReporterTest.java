package com.philmander.jstest.report;

import com.philmander.jstest.JsTestResults;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Michael Meyer
 */
public class PlainReporterTest extends AbstractReporterTest {

    @Test
    public void testCreateReport() throws IOException {
        final PlainReporter reporter = new PlainReporter();
        final JsTestResults model = getModel();
        final String report = reporter.createReport(model);

        final InputStream inputStream = PlainReporterTest.class.getResourceAsStream("PlainReporterTest.txt");
        final String expected = IOUtils.toString(inputStream);

        assertThat(report, is(expected));
    }

}
