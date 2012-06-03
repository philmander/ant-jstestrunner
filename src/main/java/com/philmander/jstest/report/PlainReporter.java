package com.philmander.jstest.report;

import com.philmander.jstest.JsTestResult;
import com.philmander.jstest.JsTestResults;
import com.philmander.jstest.PhantomTestRunner;

/**
 * Generate a plain text js test report
 * @author Phil Mander
 */
public class PlainReporter implements JsTestResultReporter {
	
	private static final String DELIMITER =
			"===============================================================================\n";		
	
	public String createReport(JsTestResults results) {
		
		StringBuilder report = new StringBuilder();
		
		report.append("JS Test Results\n");
		report.append(DELIMITER);
		
		for(JsTestResult result : results.getTestResults()) {
			report.append(result.getMessage());			
		}
		
		report.append(DELIMITER);
		
		if(results.getFailCount() == 0 && results.getErrorCount() == 0) {
			report.append(PhantomTestRunner.getSuccessMessage(results.getPassCount()));
		} else {
			report.append(PhantomTestRunner.getFailureMessage(results.getFailCount(), results.getErrorCount()));
		}
		
		return report.toString();
	}
}
