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
public class JunitReporterTest extends AbstractReporterTest {

    @Test
    public void testCreateReport() throws IOException {
        final JunitReporter reporter = new JunitReporter();
        final JsTestResults model = getModel();
        final String report = reporter.createReport(model);

        final InputStream inputStream = JunitReporterTest.class.getResourceAsStream("JunitReporterTest.xml");
        final String expected = IOUtils.toString(inputStream);

        assertThat(report, is(expected));
    }

}
