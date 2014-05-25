package com.jamierf.dropwizard;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.jamierf.dropwizard.template.Templater;
import javassist.*;
import org.vafer.jdeb.Console;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarFile;

// TODO: This assumes DW 0.7.0 - can we check what version the project is using and behave sensibly?
public class ApplicationValidator {

    private static final Templater TEMPLATER = Templater.getDefault();
    private static final String MANIFEST_MAIN_CLASS_ATTRIBUTE = "Main-Class";

    private static String loadValidateMethod(String configClass, String configFile) throws IOException {
        final String template = Resources.toString(ApplicationValidator.class.getResource("/validate.java"), StandardCharsets.UTF_8);
        return TEMPLATER.execute(template, "validate", ImmutableMap.of(
                "configClass", configClass,
                "configFile", configFile
        ));
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
        final ParameterizedType superClass = (ParameterizedType) mainClass.getGenericSuperclass();

        final Class<?> configClass = (Class<?>) superClass.getActualTypeArguments()[0];
        validateConfiguration(configClass, configFile);
    }

    private void validateConfiguration(Class<?> configClass, File configFile) throws IOException {
        try {
            final ClassPool pool = new ClassPool(false);
            pool.appendClassPath(new LoaderClassPath(classLoader));

            log.debug(String.format("Injecting configuration validation method for %s with config file %s", configClass, configFile));

            final CtClass validatorClass = pool.makeClass("ApplicationValidator");
            final String methodBody = loadValidateMethod(configClass.getName(), configFile.getAbsolutePath());
            final CtMethod validateMethod = CtNewMethod.make(methodBody, validatorClass);
            validatorClass.addMethod(validateMethod);
            validatorClass.writeFile(tempDirectory.getAbsolutePath());

            @SuppressWarnings("unchecked")
            final Object result = pool.toClass(validatorClass, classLoader, null).getMethod("validate").invoke(null);
            log.info(String.format("Successfully loaded valid configuration: %s", result));
        }
        catch (Exception e) {
            final Throwable cause = Throwables.getRootCause(e);
            throw new IllegalArgumentException(cause);
        }
    }
}
