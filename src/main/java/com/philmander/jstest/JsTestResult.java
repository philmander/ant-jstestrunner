package com.philmander.jstest;

/**
 * Bean to hold test result
 * @author Phil Mander
 *
 */
public class JsTestResult {
	
	private ResultType result;

	private String message;
	
	public enum ResultType {
		PASS, FAIL, ERROR
	}

	public JsTestResult(ResultType result, String message) {
		this.result = result;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public ResultType getResult() {
		return result;
	}
}
