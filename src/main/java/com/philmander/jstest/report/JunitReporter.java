package com.philmander.jstest.report;

import com.philmander.jstest.model.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Creates a report in the JUnit XML format.
 *
 * @author Michael Meyer
 */
public class JunitReporter implements JsTestResultReporter {

    public String createReport(TestResults results) throws IOException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // test suites
            Document document = docBuilder.newDocument();
            Element testSuitesElement = document.createElement("testsuites");
            document.appendChild(testSuitesElement);

            // test suites attributes
            addAttribute(document, testSuitesElement, "errors", results.getErrorCount());
            addAttribute(document, testSuitesElement, "failures", results.getFailCount());
            addAttribute(document, testSuitesElement, "tests", results.getTotal());

            for (TestSuite testSuite : results.getTestSuites()) {
                Element testSuiteElement = document.createElement("testsuite");
                testSuitesElement.appendChild(testSuiteElement);

                // test suite attributes
                addAttribute(document, testSuiteElement, "errors", testSuite.getErrorCount());
                addAttribute(document, testSuiteElement, "failures", testSuite.getFailCount());
                addAttribute(document, testSuiteElement, "name", testSuite.getTestFile());
                addAttribute(document, testSuiteElement, "tests", testSuite.getTotal());

                if (testSuite.getSummary() != null) {
                    addAttribute(document, testSuiteElement, "time", testSuite.getSummary().getRuntime());
                }

                if (testSuite.getError() == null) {
                    for (Test test : testSuite.getTests()) {
                        Result result = test.getResult();

                        Element testcaseElement = document.createElement("testcase");
                        testSuiteElement.appendChild(testcaseElement);
                        addAttribute(document, testcaseElement, "classname", testSuite.getTestFile());
                        addAttribute(document, testcaseElement, "name", result.getName());
                        addAttribute(document, testcaseElement, "time", result.getRuntime());

                        if (test.hasFailed()) {
                            Assertion assertion = getFirstFailingAssertion(test);
                            Element failureElement = document.createElement("failure");
                            testcaseElement.appendChild(failureElement);
                            addAttribute(document, failureElement, "type", "Javascript");
                            failureElement.appendChild(document.createTextNode(assertion.getMessage()));
                        }
                    }
                } else {
                    Element testcaseElement = document.createElement("testcase");
                    testSuiteElement.appendChild(testcaseElement);
                    addAttribute(document, testcaseElement, "classname", testSuite.getTestFile());
                    addAttribute(document, testcaseElement, "name", testSuite.getTestFile());

                    Element errorElement = document.createElement("error");
                    testcaseElement.appendChild(errorElement);
                    addAttribute(document, errorElement, "type", "Javascript");
                    errorElement.appendChild(document.createTextNode(testSuite.getError().getMessage()));
                }

                Element systemOutElement = document.createElement("system-out");
                testSuiteElement.appendChild(systemOutElement);

                Element systemErrElement = document.createElement("system-err");
                testSuiteElement.appendChild(systemErrElement);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));

            return writer.toString();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Failed to generate report", e);
        } catch (TransformerException e) {
            throw new IllegalStateException("Failed to generate report", e);
        }
    }

    private Assertion getFirstFailingAssertion(Test test) {
        for (Assertion assertion : test.getAssertions()) {
            if (!assertion.isResult()) {
                return assertion;
            }
        }

        throw new IllegalStateException("Only call this method if the test actually failed");
    }

    private void addAttribute(Document document, Element element, String name, int value) {
        addAttribute(document, element, name, String.valueOf(value));
    }

    private void addAttribute(Document document, Element element, String name, String value) {
        Attr attr = document.createAttribute(name);
        attr.setValue(value);
        element.setAttributeNode(attr);
    }

}
