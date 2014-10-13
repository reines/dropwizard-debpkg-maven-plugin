package com.jamierf.dropwizard.debpkg.stub;

import org.apache.maven.plugin.testing.stubs.ArtifactStub;

import java.io.File;

public class TestArtifactStub extends ArtifactStub {

    public TestArtifactStub(final String group, final String artifact, final String version, final String type) {
        setType(type);
        setGroupId(group);
        setArtifactId(artifact);
        setVersion(version);
        setFile(new File(String.format("%s-%s.%s", artifact, version, type)));
    }
}
