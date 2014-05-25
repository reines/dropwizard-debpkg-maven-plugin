package com.jamierf.dropwizard;

import com.jamierf.dropwizard.validator.ConfigurationValidator;
import com.jamierf.dropwizard.validator.Dropwizard7Validator;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.vafer.jdeb.Console;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class ApplicationValidator {

    private static final String MANIFEST_MAIN_CLASS_ATTRIBUTE = "Main-Class";
    public static final ComparableVersion MAX_SUPPORTED_VERSION = new ComparableVersion("0.7.0");

    public static boolean canSupportVersion(ComparableVersion version) {
        // version is <= the max supported
        return version.compareTo(MAX_SUPPORTED_VERSION) < 1;
    }

    private final File artifactFile;
    private final Console log;
    private final File tempDirectory;
    private final ClassLoader classLoader;

    public ApplicationValidator(File artifactFile, Console log, File tempDirectory) throws MalformedURLException {
        this.artifactFile = artifactFile;
        this.log = log;
        this.tempDirectory = tempDirectory;

        classLoader = new URLClassLoader(new URL[]{artifactFile.toURI().toURL()});
    }

    private String getMainClassName() throws IOException {
        final JarFile jarFile = new JarFile(artifactFile);
        final String mainClassName = jarFile.getManifest().getMainAttributes().getValue(MANIFEST_MAIN_CLASS_ATTRIBUTE);
        if (mainClassName == null) {
            throw new IllegalStateException("Failed to find main class in artifact jar");
        }

        return mainClassName;
    }

    public void validateConfiguration(File configFile) throws IOException, ClassNotFoundException {
        final Class<?> mainClass = classLoader.loadClass(getMainClassName());

        final ConfigurationValidator validator = new Dropwizard7Validator(classLoader, log, tempDirectory);
        validator.validate(mainClass, configFile);
    }
}
