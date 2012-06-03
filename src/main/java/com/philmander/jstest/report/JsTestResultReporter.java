package com.philmander.jstest.report;

import com.philmander.jstest.JsTestResults;

/**
 * 
 * @author Phil Mander
 *
 */
public interface JsTestResultReporter {
	
	public String createReport(JsTestResults results);
}
