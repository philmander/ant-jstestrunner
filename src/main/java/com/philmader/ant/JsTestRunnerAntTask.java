package com.philmader.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.SystemUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;

import com.google.common.io.Files;
import com.philmader.ant.jstest.PhantomTestRunner;

public class JsTestRunnerAntTask extends MatchingTask implements AntTaskLogger  {
	
	private File dir;

	private boolean fail = true;

	private String phantomWin = null;

	private String phantomMac = null;
	
	private String phantomLinux = null;

	private String reportFile = null;

	/**
	 * Performs JSHint validation on a set of files
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

			testRunner.runTests(absoluteFiles);
			
			log("-------------------------------------------------------------------------------");
			log("Running JS Tests Completed");
			
			int passCount = testRunner.getPassCount();
			int failCount = testRunner.getFailCount();
			int errorCount = testRunner.getErrorCount();
			log(passCount + " passed. " + failCount + "failed. " + errorCount + " errors");
			 
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
			
			//TODO: report the results to file
		} catch (IOException e) {
			
			throw new BuildException("An IO Exception caused while running JS Unit tests", e);
		}		
	}

	private void reportResults(int numFiles, int numErrors, String errorLog) {

		if (reportFile != null) {

			//TODO: write reporting

			try {
				File outFile = new File(reportFile);
				Files.createParentDirs(outFile);
				Files.touch(outFile);
				//Files.write(report.toString(), outFile, Charsets.UTF_8);
			} catch (IOException e) {
				log("Could not write report file: " + e.getMessage());
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
		} else {
			File phantomFile = new File(phantom);
			if(!phantomFile.exists()) {				
				throw new FileNotFoundException("Phantom executable does not exist + (" + phantom + ")");
			}
		}
		
		return phantom;
	}

	protected static String getFailureMessage(int failCount, int errorCount) {
		String pluralFail = failCount > 1 ? "s" : "";
		String message = "JS tests failed. " + failCount + "assertion" + pluralFail + " failed. " + errorCount + " errors";
		return message;
	}

	protected static String getSuccessMessage(int passCount) {
		String message = "All JS tests passed";
		return message;
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
	 * TODO: Implement 
	 * location of file to write test results to.
	 * @param reportFile
	 */
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
}
