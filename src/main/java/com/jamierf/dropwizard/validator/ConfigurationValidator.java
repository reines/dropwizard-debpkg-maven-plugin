package com.jamierf.dropwizard.validator;

import java.io.File;
import java.io.IOException;

public interface ConfigurationValidator {
    void validate(Class<?> mainClass, File configFile) throws IOException;
}
