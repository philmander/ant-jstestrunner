package com.philmander.jstest.ant;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.junit.Test;

import com.philmander.jstest.ant.JsTestRunnerTask;

public class JsTestRunnerTaskTest extends BuildFileTest {

	@Override
	protected void setUp() throws Exception {
		configureProject("src/test/resources/jstest.xml");
	}

	@Test
	public void testTypicalTask() throws IOException {
		try {
			executeTarget("testTypical");
		} catch (BuildException e) {
			int expectedFails = 4;
			int expectedErrors = 1;
			assertEquals(JsTestRunnerTask.getFailureMessage(expectedFails, expectedErrors), e.getMessage());
		}
	}
	
	@Test
	public void testNoFailTask() throws IOException {
		try {
			executeTarget("testNoFail");
			
		} catch (BuildException e) {
			fail("Exception shouldn't be thrown");
		}
		assertTrue("ok", true);
	}
	
	@Test
	public void testReportingTask() throws IOException {
		
		executeTarget("testReport");
		
		File reportFile = new File("target/temp/report/report.txt");
		boolean exists = reportFile.exists();
		long len = reportFile.length();
		
		assertTrue("Report file does not exist", exists);
		assertTrue("Report file was not written to", len > 0);
		
	}

}
