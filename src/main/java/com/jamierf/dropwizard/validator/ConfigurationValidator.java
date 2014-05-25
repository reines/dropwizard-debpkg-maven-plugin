package com.jamierf.dropwizard.validator;

import java.io.File;

public interface ConfigurationValidator {
    void validate(final Class<?> mainClass, final File configFile);
}
