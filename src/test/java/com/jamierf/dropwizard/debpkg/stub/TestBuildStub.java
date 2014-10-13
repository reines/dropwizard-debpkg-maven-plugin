package com.jamierf.dropwizard.debpkg.stub;

import org.apache.maven.model.Build;

import java.io.File;

public class TestBuildStub extends Build {

    public TestBuildStub(final File outputDirectory) {
        setOutputDirectory(outputDirectory.getAbsolutePath());
    }
}
