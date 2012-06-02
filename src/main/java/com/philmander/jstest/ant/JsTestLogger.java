package com.philmander.jstest.ant;

/**
 * Use to pass ant logger into other classes
 * @author Phil Mander
 *
 */
public interface JsTestLogger {
	
	public void log(String msg);
	
	public void error(String msg);	
}
