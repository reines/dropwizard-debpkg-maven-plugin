package com.jamierf.dropwizard.debpkg.validation;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.jamierf.dropwizard.debpkg.util.SystemConsole;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.vafer.jdeb.Console;

import java.io.File;
import java.io.IOException;

public class ApplicationValidatorTest {

    private static final Console LOG = new SystemConsole();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File applicationJar;

    @Before
    public void setUp() throws IOException {
        applicationJar = null; // TODO: Load dropwizard-example jar from somewhere
    }

    private File extractResource(String path) throws IOException {
        final File target = temporaryFolder.newFile(path);
        Resources.asByteSource(ApplicationValidatorTest.class.getResource(path)).copyTo(Files.asByteSink(target));
        return target;
    }

    @Ignore
    @Test
    public void testValidConfiguration() throws IOException, ClassNotFoundException {
        final ApplicationValidator validator = new ApplicationValidator(applicationJar, LOG);
        validator.validateConfiguration(extractResource("valid.yml"));
    }

    @Ignore
    @Test(expected = IllegalStateException.class)
    public void testSyntaxError() throws IOException, ClassNotFoundException {
        final ApplicationValidator validator = new ApplicationValidator(applicationJar, LOG);
        validator.validateConfiguration(extractResource("invalid_syntax.yml"));
    }

    @Ignore
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
}
