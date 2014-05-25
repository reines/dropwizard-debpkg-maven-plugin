package com.jamierf.dropwizard.validator;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.jamierf.dropwizard.ApplicationValidator;
import com.jamierf.dropwizard.template.Templater;
import javassist.*;
import org.vafer.jdeb.Console;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;

public class Dropwizard7Validator implements ConfigurationValidator {

    private static final Templater TEMPLATER = Templater.getDefault();

    private static String loadValidateMethod(String configClass, String configFile) throws IOException {
        final String template = Resources.toString(ApplicationValidator.class.getResource("/validate.java"), StandardCharsets.UTF_8);
        return TEMPLATER.execute(template, "validate", ImmutableMap.of(
                "configClass", configClass,
                "configFile", configFile
        ));
    }

    private final ClassLoader classLoader;
    private final Console log;
    private final File tempDirectory;

    public Dropwizard7Validator(ClassLoader classLoader, Console log, File tempDirectory) {
        this.classLoader = classLoader;
        this.log = log;
        this.tempDirectory = tempDirectory;
    }

    @Override
    public void validate(Class<?> mainClass, File configFile) throws IOException {
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
