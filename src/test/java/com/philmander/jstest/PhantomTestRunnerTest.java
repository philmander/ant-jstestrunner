package com.philmander.jstest;

import com.philmander.jstest.model.TestResults;
import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PhantomTestRunnerTest {

    public static String getPhantom() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "src/test/resources/phantomjs.exe";
        }
        return null;
    }

    @Test
    public void test() throws IOException {

        File phantom = new File(getPhantom());

        PhantomTestRunner testRunner;
        try {
            testRunner = new PhantomTestRunner(phantom.getAbsolutePath());
            testRunner.setLogger(new DefaultJsTestLogger());

            File testFile1 = new File("src/test/resources/tests/one-test.html");
            File testFile2 = new File("src/test/resources/tests/two-test.html");
            File testFile3 = new File("src/test/resources/tests/more/three-test.html");
            File testFileError = new File("src/test/resources/tests/more/error-test.html");

            String[] testFiles = {testFile1.getAbsolutePath(), testFile2.getAbsolutePath(),
                    testFile3.getAbsolutePath(), testFileError.getAbsolutePath()};

            TestResults results = testRunner.runTests(testFiles);

            assertEquals(4, results.getPassCount());
            assertEquals(4, results.getFailCount());
            assertEquals(1, results.getErrorCount());

        } catch (FileNotFoundException e) {
            fail("Couldn't find Phantom: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
