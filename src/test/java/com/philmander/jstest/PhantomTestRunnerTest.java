package com.philmander.jstest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import com.philmander.jstest.JsTestResult;
import com.philmander.jstest.JsTestResults;
import com.philmander.jstest.PhantomTestRunner;
import com.philmander.jstest.JsTestResult.ResultType;

public class PhantomTestRunnerTest {

	@Test
	public void test() {

		File phantom = new File(getPhantom());

		PhantomTestRunner testRunner;
		try {
			testRunner = new PhantomTestRunner(phantom.getAbsolutePath());

			File testFile1 = new File("src/test/resources/tests/one-test.html");
			File testFile2 = new File("src/test/resources/tests/two-test.html");
			File testFile3 = new File("src/test/resources/tests/more/three-test.html");
			File testFileError = new File("src/test/resources/tests/more/error-test.html");

			String[] testFiles = { testFile1.getAbsolutePath(), testFile2.getAbsolutePath(),
					testFile3.getAbsolutePath(), testFileError.getAbsolutePath() };

			JsTestResults results = testRunner.runTests(testFiles);

			int failCount = 0;
			int passCount = 0;
			int errorCount = 0;
			for (JsTestResult result : results.getTestResults()) {
				if (result.getResult() == ResultType.PASS) {
					passCount++;
				} else if (result.getResult() == ResultType.FAIL) {
					failCount++;
				} else {
					errorCount++;
				}
			}

			assertEquals(4, passCount);
			assertEquals(4, failCount);
			assertEquals(1, errorCount);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Couldn't find Phantom: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getPhantom() {
		if (SystemUtils.IS_OS_WINDOWS) {
			return "src/test/resources/phantomjs.exe";
		}
		return null;
	}

}
