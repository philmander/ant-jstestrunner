package com.philmander.jstest.ant;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.junit.Test;

import com.philmander.jstest.ant.JsTestRunnerAntTask;

public class JsTestRunnerAntTaskTest extends BuildFileTest {

	@Override
	protected void setUp() throws Exception {
		configureProject("src/test/resources/jstest.xml");
	}

	@Test
	public void testTask_typical() throws IOException {
		try {
			executeTarget("testTypical");
		} catch (BuildException e) {
			int expectedFails = 4;
			int expectedErrors = 1;
			assertEquals(JsTestRunnerAntTask.getFailureMessage(expectedFails, expectedErrors), e.getMessage());
		}
	}
	
	@Test
	public void testTask_noFail() throws IOException {
		try {
			executeTarget("testNoFail");
			
		} catch (BuildException e) {
			fail("Exception shouldn't be thrown");
		}
		assertTrue("ok", true);
	}

}
