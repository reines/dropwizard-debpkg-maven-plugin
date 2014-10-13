package com.jamierf.dropwizard.debpkg;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public abstract class AbstractDropwizardMojoTest {

    @Rule
    public MojoRule mojo = new MojoRule();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @SuppressWarnings("unchecked")
    public <T extends Mojo> T loadPlugin(final String goal, final URL config) throws Exception {
        final File pomFile = temporaryFolder.newFile();
        try (final InputStream in = config.openStream()) {
            FileUtils.copyInputStreamToFile(in, pomFile);
        }
        return (T) mojo.lookupMojo(goal, pomFile);
    }
}
