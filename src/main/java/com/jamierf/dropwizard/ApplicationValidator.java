package com.jamierf.dropwizard;

import org.vafer.jdeb.Console;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class ApplicationValidator {

    private static final String MANIFEST_MAIN_CLASS_ATTRIBUTE = "Main-Class";

    private final File artifactFile;
    private final Console log;
    private final ClassLoader classLoader;

    public ApplicationValidator(final File artifactFile, final Console log) throws MalformedURLException {
        this.artifactFile = artifactFile;
        this.log = log;

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

    public void validateConfiguration(final File configFile) throws IOException, ClassNotFoundException {
        final Class<?> mainClass = classLoader.loadClass(getMainClassName());

        try {
            final Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
            // Passed in an Object[] to avoid invoking as varargs
            mainMethod.invoke(null, new Object[]{new String[]{"check", configFile.getAbsolutePath()}});
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.warn("Failed to validate configuration");
            throw new IllegalStateException(e);
        }
    }
}
