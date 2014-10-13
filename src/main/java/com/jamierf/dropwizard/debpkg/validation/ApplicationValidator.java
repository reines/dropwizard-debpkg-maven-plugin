package com.jamierf.dropwizard.debpkg.validation;

import com.google.common.base.Throwables;
import org.apache.tools.ant.ExitException;
import org.vafer.jdeb.Console;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ApplicationValidator {

    private static final String MANIFEST_MAIN_CLASS_ATTRIBUTE = "Main-Class";

    private final File artifactFile;
    private final Console log;
    private final ClassLoader classLoader;

    public ApplicationValidator(final File artifactFile, final Console log) throws IOException, ClassNotFoundException {
        this.artifactFile = artifactFile;
        this.log = log;

        // Set parent to null to avoid pulling in SLF4J and other conflicts from our self
        classLoader = new URLClassLoader(new URL[]{artifactFile.toURI().toURL()}, null);
    }

    private Class<?> getMainClass() throws IOException, ClassNotFoundException {
        final JarFile jarFile = new JarFile(artifactFile);
        final Manifest manifest = jarFile.getManifest();
        if (manifest == null) {
            throw new IllegalStateException("Failed to find manifest file");
        }

        final String mainClassName = manifest.getMainAttributes().getValue(MANIFEST_MAIN_CLASS_ATTRIBUTE);
        if (mainClassName == null) {
            throw new IllegalStateException("Failed to find main class in artifact jar");
        }

        return classLoader.loadClass(mainClassName);
    }

    public void validateConfiguration(final File configFile) throws IOException, ClassNotFoundException {
        validateConfiguration(getMainClass(), configFile);
    }

    protected void validateConfiguration(final Class<?> clazz, final File configFile) {
        final SecurityManager securityManager = System.getSecurityManager();

        try {
            System.setSecurityManager(new DelegatingNoExitSecurityManager(securityManager));

            final Method mainMethod = clazz.getDeclaredMethod("main", String[].class);
            // Passed in an Object[] to avoid invoking as varargs
            mainMethod.invoke(null, new Object[]{new String[]{"check", configFile.getAbsolutePath()}});
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            final Throwable cause = Throwables.getRootCause(e);
            if (cause instanceof ExitException) {
                if (((ExitException) cause).getStatus() != 0) {
                    log.warn("Failed to validate configuration");
                    throw new IllegalStateException(cause);
                }
            }

            log.warn("Failed to call configuration validation method");
            throw Throwables.propagate(e);
        }
        finally {
            System.setSecurityManager(securityManager);
        }
    }
}
