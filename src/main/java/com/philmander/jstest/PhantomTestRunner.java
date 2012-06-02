package com.philmander.jstest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;

import com.google.common.io.Files;
import com.philmander.jstest.JsTestResult.ResultType;
import com.philmander.jstest.ant.JsTestLogger;

/**
 * Standalone class for running js unit tests with Phantom
 * @author Phil Mander
 *
 */
public class PhantomTestRunner {

	private  JsTestLogger logger = null;

	private static final String PHANTOM_QUNIT_RUNNER = "qunit-phantom-runner.js";
	
	private static final String PASS_PREFIX = "PASS: ";
	
	private static final String FAIL_PREFIX = "FAIL: ";
	
	private static final String ERROR_PREFIX = "ERROR: ";

	private String phantom = null;

	
	
	public static void main(String[] args) {		
		
		File currentDir = new File(".");
		
		CommandLineParser parser = new PosixParser();

		// create the Options
		Options options = new Options();
		options.addOption( "p", "phantom", true, "Location of Phantom JS executable" );
		
		try {			
			CommandLine cl = parser.parse(options, args);
			
			String phantomOption = cl.getOptionValue('p');
			File phantomLoc = new File(currentDir, phantomOption);
			
			PhantomTestRunner testRunner = new PhantomTestRunner(phantomLoc.getAbsolutePath());
			testRunner.setLogger(new JsTestLogger() {
				
				public void log(String msg) {
					System.out.println(msg);
					
				}
			});
			
			List<String> testFiles = new ArrayList<String>();
			for(Object arg : cl.getArgList()) {
				
				File testFile = new File(currentDir, (String)arg);
				if(testFile.exists()) {
					testFiles.add(testFile.getAbsolutePath());
				} else {
					System.out.println("Couldn't find " + testFile.getAbsolutePath()); 
				}
			}
			
			System.out.println("Using Phantom JS at : " + phantomLoc.getAbsolutePath());
			System.out.println("Running tests on " + testFiles.size() + " files");
			
			testRunner.runTests(testFiles.toArray(new String[testFiles.size()]));
			
						
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * @param phantom The location of the phantom executable
	 * @throws FileNotFoundException 
	 */
	public PhantomTestRunner(String phantom) throws FileNotFoundException {
		
		File phantomFile = new File(phantom);
		if(!phantomFile.exists()) {				
			throw new FileNotFoundException("Phantom executable does not exist for " + SystemUtils.OS_NAME + "(" + phantom + ")");
		}
		
		this.phantom = phantom;
	}
	
	public JsTestResults runTests(String[] testFiles) throws IOException {		
				
		JsTestResults results = new JsTestResults();
		
		
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
					} else if(line.startsWith(FAIL_PREFIX)){
						message = line.substring(FAIL_PREFIX.length());
						result = ResultType.FAIL;					
					} else {
						result = ResultType.ERROR;
						message = line.substring(ERROR_PREFIX.length());					
					}
					
					JsTestResult testResult = new JsTestResult(result, message);
					results.addResult(testResult);
				}
			}
			
			process.destroy();
		}
				
		return results;
	}
	
	/**
	 * Returns a standard summary message on test failure
	 * @param failCount
	 * @param errorCount
	 * @return
	 */
	public static String getFailureMessage(int failCount, int errorCount) {
		String pluralFail = failCount > 1 ? "s" : "";
		String pluralErrors = errorCount > 1 ? "s" : "";
		String message = "JS tests failed. " + failCount + " assertion" + pluralFail + " failed. " + errorCount + " error" + pluralErrors + ".";
		return message;
	}

	/**
	 * Returns a standard summary message on test success
	 * @param passCount
	 * @return
	 */
	public static String getSuccessMessage(int passCount) {
		String message = "All JS tests passed";
		return message;
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

	/**
	 * Sets a logger for the test runner to report results to.  Use before calling runTests()
	 * @param logger
	 */
	public void setLogger(JsTestLogger logger) {
		this.logger = logger;
	}
}
