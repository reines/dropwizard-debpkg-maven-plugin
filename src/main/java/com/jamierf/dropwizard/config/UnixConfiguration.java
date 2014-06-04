package com.jamierf.dropwizard.config;

import org.apache.maven.plugins.annotations.Parameter;

public class UnixConfiguration {

    @Parameter
    @SuppressWarnings("FieldCanBeLocal")
    private String user = "dropwizard";

    @SuppressWarnings("unused")
    public String getUser() {
        return user;
    }
}
