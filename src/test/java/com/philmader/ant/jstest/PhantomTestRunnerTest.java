package com.philmader.ant.jstest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import com.philmader.ant.AntTaskLogger;
import com.philmader.ant.jstest.PhantomTestRunner;
import com.philmader.ant.jstest.TestResult;
import com.philmader.ant.jstest.TestResult.ResultType;

public class PhantomTestRunnerTest {

	@Test
	public void test() {

		File phantom = new File(getPhantom());
		
		PhantomTestRunner testRunner = new PhantomTestRunner(phantom.getAbsolutePath());
		testRunner.setLogger(new AntTaskLogger() {
			
			public void log(String msg) {
				System.out.println(msg);
				
			}
		});
		
		File testFile1 = new File("src/test/resources/tests/one-test.html");
		File testFile2 = new File("src/test/resources/tests/two-test.html");
		File testFile3 = new File("src/test/resources/tests/more/three-test.html");
		File testFileError = new File("src/test/resources/tests/more/error-test.html");
		
		String[] testFiles = {
				testFile1.getAbsolutePath(),
				testFile2.getAbsolutePath(),
				testFile3.getAbsolutePath(),
				testFileError.getAbsolutePath()
		};
		
		try {
			testRunner.runTests(testFiles);
			
			List<TestResult> results = testRunner.getTestResults();
			
			int failCount = 0;
			int passCount = 0;
			int errorCount = 0;
			for(TestResult result : results) {
				if(result.getResult() == ResultType.PASS) {
					passCount++;
				} else if(result.getResult() == ResultType.FAIL){
					failCount++;
				} else {
					errorCount++;
				}
			}
	
			assertEquals(6, passCount);
			assertEquals(3, failCount);
			assertEquals(1, errorCount);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String getPhantom() {
		if(SystemUtils.IS_OS_WINDOWS) {
			return "src/test/resources/phantomjs.exe";
		}
		return null;
	}

}
