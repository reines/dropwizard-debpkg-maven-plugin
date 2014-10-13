package com.jamierf.dropwizard.debpkg.stub;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;

import java.io.File;

public class TestProjectStub extends MavenProjectStub {

    public TestProjectStub() {
        this("com.jamierf", "test", "1.0", "jar");
    }

    public TestProjectStub(final String group, final String artifact, final String version, final String type) {
        setArtifact(new TestArtifactStub(group, artifact, version, type));
        setGroupId(group);
        setArtifactId(artifact);
        setVersion(version);

        setBuild(new TestBuildStub(new File(getBasedir(), "out")));
    }

    @Override
    public File getBasedir() {
        return FileUtils.getTempDirectory();
    }
}
