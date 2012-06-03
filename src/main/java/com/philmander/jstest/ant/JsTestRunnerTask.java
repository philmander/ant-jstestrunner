package com.philmander.jstest.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.LogLevel;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.philmander.jstest.JsTestLogger;
import com.philmander.jstest.JsTestResults;
import com.philmander.jstest.PhantomTestRunner;
import com.philmander.jstest.report.PlainReporter;
import com.philmander.jstest.report.JsTestResultReporter;

/**
 * JsTestRunner Ant Task
 * @author Phil Mander
 *
 */
public class JsTestRunnerTask extends MatchingTask implements JsTestLogger  {
	
	private File dir;

	private boolean fail = true;

	private String phantomWin = null;

	private String phantomMac = null;
	
	private String phantomLinux = null;

	private List<ReportType> reports = new ArrayList<ReportType>();
	
	public void addConfiguredReport(ReportType report) {
		reports.add(report);
	}

	/**
	 * Run Phantom test runner on a set of files
	 */
	@Override
	public void execute() throws BuildException {
		
		checkAttributes();

		DirectoryScanner dirScanner = getDirectoryScanner(dir);
		String[] includedFiles = dirScanner.getIncludedFiles();
		
		String[] absoluteFiles = new String[includedFiles.length];
		for(int i = 0; i < includedFiles.length; i++) {
			absoluteFiles[i] = dir.getAbsolutePath() + "/" + includedFiles[i];
		}
		
		//summarize results
		try {			
			PhantomTestRunner testRunner = new PhantomTestRunner(getPhantom());
			testRunner.setLogger(this);
			
			log("Running JS Tests...");
			log("-------------------------------------------------------------------------------");

			JsTestResults results = testRunner.runTests(absoluteFiles);
			
			log("-------------------------------------------------------------------------------");
			log("Running JS Tests Completed");
			
			int passCount = results.getPassCount();
			int failCount = results.getFailCount();
			int errorCount = results.getErrorCount();
			log(passCount + " passed. " + failCount + " failed. " + errorCount + " errors");
			 
			reportResults(results);
			
			if(failCount > 0 || errorCount > 0) {
				String message  = getFailureMessage(failCount, errorCount);
				if(fail) {
					throw new BuildException(message);
				} else {
					log(message);
				}
				
			} else {
				log(getSuccessMessage(passCount));
			}						
		} catch (IOException e) {
			
			throw new BuildException("An IO Exception caused while running JS Unit tests", e);
		}		
	}

	private void reportResults(JsTestResults results) {

		for(ReportType report : reports) {

			JsTestResultReporter reporter;
			
			//pick a reporter implementation
			if(report.getType().trim().equalsIgnoreCase("plain")) {
				reporter = new PlainReporter();
			} else {
				//default to plain reporter
				reporter = new PlainReporter();
			}				
			
			if(report.getDestFile() == null) {
				error("Could not write a report, destFile attribute was not set");
				continue;
			}

			try {
				log("Writing report to " + report.getDestFile().getAbsolutePath());
				File outFile = report.getDestFile();
				Files.createParentDirs(outFile);
				Files.touch(outFile);
				Files.write(reporter.createReport(results), outFile, Charsets.UTF_8);
			} catch (IOException e) {
				error("Could not write report file: " + e.getMessage());
			}
		}
	}

	private void checkAttributes() {
		String message = null;

		if (dir == null) {
			message = "Missing dir attribute";
		} else if (!dir.exists()) {
			message = "Directoy does not exist";
		} else if (!dir.isDirectory()) {
			message = "Dir is not a directory";
		}
		
		if (message != null) {
			throw new BuildException(message);
		}
	}
	
	private String getPhantom() throws FileNotFoundException {
		
		String phantom = null;
		
		if(SystemUtils.IS_OS_WINDOWS) {
			phantom = phantomWin;
		} else if(SystemUtils.IS_OS_MAC) {
			phantom = phantomMac;
		} else if(SystemUtils.IS_OS_LINUX) {
			phantom = phantomLinux;
		}
		
		if(phantom == null) {
			throw new BuildException("No phantom executable specified. (Using " + SystemUtils.OS_NAME + ")");
		} 		
		
		return phantom;
	}

	protected static String getFailureMessage(int failCount, int errorCount) {
		return PhantomTestRunner.getFailureMessage(failCount, errorCount);
	}

	protected static String getSuccessMessage(int passCount) {
		return PhantomTestRunner.getSuccessMessage(passCount);
	}

	/**
	 * The directory to scan for files to validate. Use includes to only
	 * validate js files and excludes to omit files such as compressed js
	 * libraries from js validation
	 * 
	 * @param dir
	 */
	public void setDir(File dir) {
		this.dir = dir;
	}

	/**
	 * By default the ant task will fail the build if any assertions fail
	 * Set this to false for reporting purposes
	 * 
	 * @param fail
	 */
	public void setFail(boolean fail) {
		this.fail = fail;
	}


	/**
	 * Location of Phantom executable when running in Windows environments
	 * @param phantomWin A file location
	 */
	public void setPhantomWin(String phantomWin) {
		this.phantomWin = phantomWin;
	}

	/**
	 * Location of Phantom executable when running in Mac environments
	 * @param phantomMac A file location
	 */
	public void setPhantomMac(String phantomMac) {
		this.phantomMac = phantomMac;
	}

	/**
	 * Location of Phantom executable when running in Linux environments
	 * @param phantomWin A file location
	 */
	public void setPhantomLinux(String phantomLinux) {
		this.phantomLinux = phantomLinux;
	}

	/**
	 * Logs an error
	 */
	public void error(String msg) {
		log(msg, LogLevel.ERR.getLevel());
	}
}
