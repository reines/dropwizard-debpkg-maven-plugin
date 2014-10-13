package com.jamierf.dropwizard.debpkg.stub;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;

import java.io.File;
import java.io.IOException;

public class TestProjectStub extends MavenProjectStub {

    public TestProjectStub() throws IOException {
        this("com.jamierf", "test", "1.0", "jar");
    }

    public TestProjectStub(final String group, final String artifact, final String version, final String type) throws IOException {
        final File file = new File(getBasedir(), String.format("%s-%s.%s", artifact, version, type));
        FileUtils.touch(file);

        setArtifact(new TestArtifactStub(group, artifact, version, type, file));
        setGroupId(group);
        setArtifactId(artifact);
        setVersion(version);
        setFile(file);

        setBuild(new TestBuildStub(new File(getBasedir(), "out")));
    }

    @Override
    public File getBasedir() {
        return FileUtils.getTempDirectory();
    }
}
