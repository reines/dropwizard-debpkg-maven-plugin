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

    public JvmConfiguration setMemory(final String memory) {
        this.memory = memory;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public JvmConfiguration setPackageName(final String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public JvmConfiguration setPackageVersion(final String packageVersion) {
        this.packageVersion = packageVersion;
        return this;
    }
}
