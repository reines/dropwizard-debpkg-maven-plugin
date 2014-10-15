package com.jamierf.dropwizard.debpkg.validation;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.jamierf.dropwizard.debpkg.util.SystemConsole;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.vafer.jdeb.Console;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class ApplicationValidatorTest {

    private static final Console LOG = new SystemConsole();
    private static final URI APPLICATION_JAR_URI = URI.create("https://dl.dropboxusercontent.com/s/r7snqf4arye0w6n/dropwizard-example-0.7.0.jar");

    private static class NoMainMethodClass {}

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File applicationJar;

    @Before
    public void setUp() throws IOException {
        applicationJar = new File(System.getProperty("java.io.tmpdir"), "dropwizard-example-0.7.0.jar");
        if (!applicationJar.exists()) {
            try (final InputStream in = new BufferedInputStream(APPLICATION_JAR_URI.toURL().openStream())) {
                LOG.info(String.format("Downloading %s to %s", APPLICATION_JAR_URI, applicationJar.getAbsolutePath()));
                Files.asByteSink(applicationJar).writeFrom(in);
            }
        }
    }

    private File extractResource(String path) throws IOException {
        final File target = temporaryFolder.newFile();
        Resources.asByteSource(ApplicationValidatorTest.class.getResource(path)).copyTo(Files.asByteSink(target));
        return target;
    }

    @Test
    public void testValidConfiguration() throws IOException, ClassNotFoundException {
        final ApplicationValidator validator = new ApplicationValidator(applicationJar, LOG);
        validator.validateConfiguration(extractResource("valid.yml"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSyntaxError() throws IOException, ClassNotFoundException {
        final ApplicationValidator validator = new ApplicationValidator(applicationJar, LOG);
        validator.validateConfiguration(extractResource("invalid_syntax.yml"));
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidConfiguration() throws IOException, ClassNotFoundException {
        final ApplicationValidator validator = new ApplicationValidator(applicationJar, LOG);
        validator.validateConfiguration(extractResource("invalid.yml"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoMainClass() throws IOException, ClassNotFoundException {
        final File jarFile = extractResource("no_main_class.jar");
        final ApplicationValidator validator = new ApplicationValidator(jarFile, LOG);
        validator.validateConfiguration(extractResource("valid.yml"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoManifestClass() throws IOException, ClassNotFoundException {
        final File jarFile = extractResource("no_manifest.jar");
        final ApplicationValidator validator = new ApplicationValidator(jarFile, LOG);
        validator.validateConfiguration(extractResource("valid.yml"));
    }

    @Test(expected = RuntimeException.class)
    public void testPrivateMainMethod() throws IOException, ClassNotFoundException {
        final File jarFile = extractResource("private.jar");
        final ApplicationValidator validator = new ApplicationValidator(jarFile, LOG);
        validator.validateConfiguration(extractResource("valid.yml"));
    }

    @Test(expected = RuntimeException.class)
    public void testNoMainMethod() throws IOException, ClassNotFoundException {
        final ApplicationValidator validator = new ApplicationValidator(applicationJar, LOG);
        validator.validateConfiguration(NoMainMethodClass.class, extractResource("valid.yml"));
    }
}
