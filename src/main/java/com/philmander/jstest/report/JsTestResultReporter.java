package com.philmander.jstest.report;

import com.philmander.jstest.JsTestResults;

import java.io.IOException;

/**
 * @author Phil Mander
 */
public interface JsTestResultReporter {

	public String createReport(JsTestResults results) throws IOException;

}
