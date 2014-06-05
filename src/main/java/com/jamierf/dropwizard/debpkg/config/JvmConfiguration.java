package com.jamierf.dropwizard.debpkg.config;

import org.apache.maven.plugins.annotations.Parameter;

public class JvmConfiguration {

    // TODO: Ability to specify arguments rather than simply 'memory'

    @Parameter
    private String memory = "128m";

    @Parameter
    private String packageName = "openjdk-7-jdk";

    @Parameter
    private String packageVersion = null;

    public String getMemory() {
        return memory;
    }

    public void setMemory(final String memory) {
        this.memory = memory;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public void setPackageVersion(final String packageVersion) {
        this.packageVersion = packageVersion;
    }
}
