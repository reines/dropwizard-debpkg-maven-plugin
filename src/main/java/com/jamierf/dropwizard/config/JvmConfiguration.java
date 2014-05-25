package com.jamierf.dropwizard.config;

import org.apache.maven.plugins.annotations.Parameter;

public class JvmConfiguration {

    // TODO: Ability to specify arguments rather than simply 'memory'

    @Parameter
    private String memory = "128m";

    @Parameter
    private String packageName = "openjdk-7-jdk";

    @Parameter
    private String packageVersion = null;

    @SuppressWarnings("unused")
    public String getMemory() {
        return memory;
    }

    @SuppressWarnings("unused")
    public String getPackageName() {
        return packageName;
    }

    @SuppressWarnings("unused")
    public String getPackageVersion() {
        return packageVersion;
    }
}
