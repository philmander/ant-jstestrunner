package com.philmander.jstest.model;

/**
 * @author Michael Meyer
 */
public class Module {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Module{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
    
}
