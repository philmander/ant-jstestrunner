package com.philmander.jstest.report;

import com.google.common.collect.Lists;
import com.philmander.jstest.JsTestResults;
import com.philmander.jstest.model.*;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author Michael Meyer
 */
/*package*/ abstract class AbstractReporterTest {

    protected JsTestResults getModel() {
        final JsTestResults jsTestResults = new JsTestResults();
        jsTestResults.addTestFile(createTestFile1());

        return jsTestResults;
    }

    private TestFile createTestFile1() {
        final TestFile testFile = new TestFile();
        testFile.setFile("c:/test1.html");
        testFile.setSummary(createSummary(2, 1, 49));

        final List<Test> tests = Lists.newArrayList();
        tests.add(createPassingTest("jstestrunner", "test onLoad", 2));
        tests.add(createFailingTest(null, "test refresh", 1));
        testFile.setTests(tests);

        return testFile;
    }

    private Test createPassingTest(String module, String name, int numberOfAssertions) {
        final Test test = new Test();
        test.setModule(createModule(module));
        test.setResult(createResult(name, numberOfAssertions, 0, 12));

        final List<Assertion> assertions = Lists.newArrayList();
        for (int i = 0; i < numberOfAssertions; i++) {
            assertions.add(createAssertion("assertion " + i, null, null, null));
        }
        test.setAssertions(assertions);

        return test;
    }

    private Test createFailingTest(String module, String name, int numberOfAssertions) {
        final Test test = new Test();
        test.setModule(createModule(module));
        test.setResult(createResult(name, 0, numberOfAssertions, 19));

        final List<Assertion> assertions = Lists.newArrayList();
        for (int i = 0; i < numberOfAssertions; i++) {
            assertions.add(createAssertion("assertion " + i, "Some error message...", "clicked", "clickedd"));
        }
        test.setAssertions(assertions);

        return test;
    }

    private Assertion createAssertion(String name, String message, String expected, String actual) {
        final Assertion assertion = new Assertion();
        assertion.setName(name);
        assertion.setMessage(message);
        assertion.setExpected(expected);
        assertion.setActual(actual);

        if (StringUtils.isNotBlank(expected) || StringUtils.isNotBlank(actual)) {
            assertion.setResult(false);
        } else {
            assertion.setResult(true);
        }

        return assertion;
    }

    private Result createResult(String name, int passed, int failed, int runtime) {
        final Result result = new Result();
        result.setName(name);
        result.setPassed(passed);
        result.setFailed(failed);
        result.setTotal(passed + failed);
        result.setRuntime(runtime);

        return result;
    }

    private Module createModule(String name) {
        final Module module = new Module();
        module.setName(name);

        return module;
    }

    private Summary createSummary(int passed, int failed, int runtime) {
        final Summary summary = new Summary();
        summary.setPassed(passed);
        summary.setFailed(failed);
        summary.setTotal(passed + failed);
        summary.setRuntime(runtime);

        return summary;
    }

}
