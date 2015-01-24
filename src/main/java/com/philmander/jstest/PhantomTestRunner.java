package com.philmander.jstest;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.philmander.jstest.model.TestFile;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Standalone class for running js unit tests with Phantom
 *
 * @author Phil Mander
 */
public class PhantomTestRunner {

    private static final String PHANTOM_QUNIT_RUNNER = "qunit-phantom-runner.js";

    private String phantom = null;

    private JsTestLogger logger = null;

    /**
     * @param phantom The location of the phantom executable
     * @throws FileNotFoundException
     */
    public PhantomTestRunner(String phantom) throws FileNotFoundException {
        File phantomFile = new File(phantom);
        if (!phantomFile.exists()) {
            throw new FileNotFoundException("Phantom executable does not exist for " + SystemUtils.OS_NAME + "(" + phantom + ")");
        }

        this.phantom = phantom;
    }

    /**
     * CLI for phantom test runner
     *
     * @param args
     */
    public static void main(String[] args) {
        File currentDir = new File(".");

        CommandLineParser parser = new PosixParser();

        // create the Options
        Options options = new Options();
        options.addOption("p", "phantom", true, "Location of Phantom JS executable");

        try {
            JsTestLogger logger = new DefaultJsTestLogger();

            CommandLine cl = parser.parse(options, args);

            String phantomOption = cl.getOptionValue('p');
            File phantomLoc = new File(currentDir, phantomOption);

            PhantomTestRunner testRunner = new PhantomTestRunner(phantomLoc.getAbsolutePath());
            testRunner.setLogger(logger);

            List<String> testFiles = new ArrayList<String>();
            for (Object arg : cl.getArgList()) {

                File testFile = new File(currentDir, (String) arg);
                if (testFile.exists()) {
                    testFiles.add(testFile.getAbsolutePath());
                } else {
                    logger.error("Couldn't find " + testFile.getAbsolutePath());
                }
            }

            logger.log("Using Phantom JS at : " + phantomLoc.getAbsolutePath());
            logger.log("Running tests on " + testFiles.size() + " files");

            testRunner.runTests(testFiles.toArray(new String[testFiles.size()]));

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String fixFilePath(String loc) {
        File file = new File(loc);
        String path = "file:///" + file.getAbsolutePath().replace("\\", "/");

        return path;
    }

    public JsTestResults runTests(String[] files) throws IOException {
        JsTestResults results = new JsTestResults();

        //set up process params
        String qunitRunner = setupPhantomRunnerFile();

        for (String file : files) {
            logger.log("Running " + file + "...");

            String loc = fixFilePath(file);
            String[] params = {
                    phantom, qunitRunner, loc
            };

            //set up process
            ProcessBuilder processBuilder = new ProcessBuilder(params);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            //read and parse output from phantom
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line = reader.readLine();
            while (line != null && !line.equalsIgnoreCase("EOF")) {
                output.append(line);
                output.append(MessageUtil.NEWLINE);

                line = reader.readLine();
            }

            Gson gson = new Gson();
            TestFile testFile = gson.fromJson(output.toString(), TestFile.class);
            testFile.setFile(file);
            results.addTestFile(testFile);

            destroy(process);

            logger.log(MessageUtil.getTestFileReport(testFile));
            logger.log(MessageUtil.NEWLINE);
        }

        return results;
    }

    private String setupPhantomRunnerFile() throws IOException {
        File tempDir = Files.createTempDir();
        File tempLoc = new File(tempDir, PHANTOM_QUNIT_RUNNER);
        FileUtils.copyInputStreamToFile(
                this.getClass().getClassLoader().getResourceAsStream(PHANTOM_QUNIT_RUNNER),
                tempLoc);

        return tempLoc.getAbsolutePath();
    }

    private void destroy(Process process) {
        try {
            // Wait for phantom.exit() to complete
            process.waitFor();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        process.destroy();
    }

    /**
     * Sets a logger for the test runner to report results to.  Use before calling runTests()
     *
     * @param logger
     */
    public void setLogger(JsTestLogger logger) {
        this.logger = logger;
    }

}
