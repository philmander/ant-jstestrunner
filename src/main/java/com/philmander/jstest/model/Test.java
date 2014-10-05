package com.philmander.jstest.model;

import java.util.List;

/**
 * Model for a single QUnit test.
 *
 * @author Michael Meyer
 */
public class Test {

    /**
     * Can be <code>null</code> if no module was defined.
     */
    private Module module;

    private List<Assertion> assertions;

    private Result result;

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<Assertion> getAssertions() {
        return assertions;
    }

    public void setAssertions(List<Assertion> assertions) {
        this.assertions = assertions;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * @return Returns <code>true</code> if all assertions where successful.
     */
    public boolean isSuccess() {
        for (Assertion assertion : assertions) {
            if (!assertion.isResult()) {
                // At least one assertion failed
                return false;
            }
        }

        // All assertions have been successful
        return true;
    }

    /**
     * @return Returns <code>true</code> if at least one assertion failed.
     */
    public boolean hasFailed() {
        for (Assertion assertion : assertions) {
            if (!assertion.isResult()) {
                // At least one assertion failed
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Test{");
        sb.append("module=").append(module);
        sb.append(", assertions=").append(assertions);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }

}
