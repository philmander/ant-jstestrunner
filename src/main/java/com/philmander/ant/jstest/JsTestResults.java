package com.philmander.ant.jstest;

import java.util.ArrayList;
import java.util.List;

public class JsTestResults {
	
	private List<JsTestResult> testResults = new ArrayList<JsTestResult>();
	
	private int failCount = 0;
	
	private int passCount = 0;
	
	private int errorCount = 0;

	public JsTestResults() {
		
	}
	
	public void addResult(JsTestResult result) {
		
		testResults.add(result);
		
		if(result.getResult() == JsTestResult.ResultType.PASS) {
			passCount++;
		} else if(result.getResult() == JsTestResult.ResultType.FAIL) {
			failCount++;			
		} else if(result.getResult() == JsTestResult.ResultType.ERROR) {
			errorCount++;
		}
	}

	public List<JsTestResult> getTestResults() {
		return testResults;
	}

	public int getFailCount() {
		return failCount;
	}

	public int getPassCount() {
		return passCount;
	}

	public int getErrorCount() {
		return errorCount;
	}
	
	
}
