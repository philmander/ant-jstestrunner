package com.philmander.jstest.report;

import com.philmander.jstest.model.TestResults;

import java.io.IOException;

/**
 * @author Phil Mander
 */
public interface JsTestResultReporter {

	public String createReport(TestResults results) throws IOException;

}
