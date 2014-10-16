package com.jamierf.dropwizard.debpkg.stub;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;

import java.io.File;
import java.io.IOException;

public class TestProjectStub extends MavenProjectStub {

    public TestProjectStub(final String group, final String artifact, final String version, final String type) throws IOException {
        final File file = new File(getBasedir(), String.format("%s-%s.%s", artifact, version, type));
        FileUtils.touch(file);

        setArtifact(new TestArtifactStub(group, artifact, version, type, file));
        setGroupId(group);
        setArtifactId(artifact);
        setVersion(version);
        setFile(file);

        final File outputDirectory = new File(getBasedir(), "out");
        final File buildDirectory = new File(getBasedir(), "build");

        setBuild(new TestBuildStub(buildDirectory, outputDirectory));
    }

    @Override
    public File getBasedir() {
        return FileUtils.getTempDirectory();
    }
}
