package com.philmader.ant.jstest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;
import com.philmader.ant.AntTaskLogger;
import com.philmader.ant.jstest.TestResult.ResultType;

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
			//processBuilder.directory(new File(new File(testFile).getParent()));
			
			Process process = processBuilder.start();
			
			//read and parse output from phantom
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line = null;
			while ((line = input.readLine()) != null) {
				if(logger != null) {
					logger.log(line);
				}
				
				if(line.startsWith(PASS_PREFIX) || line.startsWith(FAIL_PREFIX) || line.startsWith(ERROR_PREFIX)) {
					
					ResultType result;
					if(line.startsWith(PASS_PREFIX)) {
						result = ResultType.PASS;
						passCount++;
					} else if(line.startsWith(FAIL_PREFIX)){
						result = ResultType.FAIL;
						failCount++;
					} else {
						result = ResultType.ERROR;
						errorCount++;
					}
					
					String message = line.substring(PASS_PREFIX.length());
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

	public int getFailCount() {
		return failCount;
	}

	public int getPassCount() {
		return passCount;
	}
	
	public int getErrorCount() {
		return errorCount;
	}

	public void setLogger(AntTaskLogger logger) {
		this.logger = logger;
	}
}
