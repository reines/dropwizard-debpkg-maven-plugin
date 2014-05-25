package com.jamierf.dropwizard.template;

public class MissingParameterException extends IllegalArgumentException {

    private final String name;

    public MissingParameterException(String name) {
        super ("Missing template parameter: " + name);

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
