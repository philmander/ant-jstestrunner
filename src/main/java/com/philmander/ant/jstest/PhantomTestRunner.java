package com.philmander.ant.jstest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;
import com.philmander.ant.AntTaskLogger;
import com.philmander.ant.jstest.TestResult.ResultType;

/**
 * Standalone class for running js unit tests with Phantom
 * @author Phil Mander
 *
 */
public class PhantomTestRunner {
	
	private  AntTaskLogger logger = null;

	private static final String PHANTOM_QUNIT_RUNNER = "qunit-phantom-runner.js";
	
	private static final String PASS_PREFIX = "PASS: ";
	
	private static final String FAIL_PREFIX = "FAIL: ";
	
	private static final String ERROR_PREFIX = "ERROR: ";

	private String phantom = null;

	private List<TestResult> testResults = new ArrayList<TestResult>();
	
	private int failCount = 0;
	
	private int passCount = 0;
	
	private int errorCount = 0;
	
	/**
	 * @param phantom The location of the phantom executable
	 */
	public PhantomTestRunner(String phantom) {
		
		this.phantom = phantom;
	}
	
	public void runTests(String[] testFiles) throws IOException {		
		
		//set up process params
		String qunitRunner = setupPhantomRunnerFile();
		
		for (String testFile : testFiles) {
			
			String loc = fixFilePathForWindows(testFile);
			String[] params = {
					phantom, qunitRunner, loc
			};
			
			//set up process
			ProcessBuilder processBuilder = new ProcessBuilder(params);
			Process process = processBuilder.start();
			
			//read and parse output from phantom
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line = null;
			while ((line = input.readLine()) != null) {
				if(logger != null) {
					logger.log(line);
				}
				
				if(line.startsWith(PASS_PREFIX) || line.startsWith(FAIL_PREFIX) || line.startsWith(ERROR_PREFIX)) {
				
					String message;
					ResultType result;
					if(line.startsWith(PASS_PREFIX)) {
						result = ResultType.PASS;
						message = line.substring(PASS_PREFIX.length());
						passCount++;
					} else if(line.startsWith(FAIL_PREFIX)){
						message = line.substring(FAIL_PREFIX.length());
						result = ResultType.FAIL;
						failCount++;
					} else {
						result = ResultType.ERROR;
						message = line.substring(ERROR_PREFIX.length());
						errorCount++;
					}
					
					TestResult testResult = new TestResult(result, message);
					testResults.add(testResult);
				}
			}
			
			process.destroy();
		}
	}
	
	private String setupPhantomRunnerFile()  throws IOException {
		
		File tempDir = Files.createTempDir();
		File tempLoc = new File(tempDir, PHANTOM_QUNIT_RUNNER);		
		FileUtils.copyInputStreamToFile(
				this.getClass().getClassLoader().getResourceAsStream(PHANTOM_QUNIT_RUNNER),
				tempLoc);
				
		return tempLoc.getAbsolutePath();
	}
	
	private static String fixFilePathForWindows(String loc) {
		File file = new File(loc);
		String path = "file:///" + file.getAbsolutePath().replace("\\", "/");
		return path;
	}

	public List<TestResult> getTestResults() {
		return testResults;
	}

	/**
	 * Returns the number of assertions that failed. Use after calling runTests()
	 * @return
	 */
	public int getFailCount() {
		return failCount;
	}

	/**
	 * Returns the number of assertions that passed. Use after calling runTests()
	 * @return
	 */
	public int getPassCount() {
		return passCount;
	}
	
	/**
	 * Returns the number of errors encountered. Use after calling runTests()
	 * @return
	 */
	public int getErrorCount() {
		return errorCount;
	}

	/**
	 * Sets a logger for the test runner to report results to.  Use before calling runTests()
	 * @param logger
	 */
	public void setLogger(AntTaskLogger logger) {
		this.logger = logger;
	}
}
