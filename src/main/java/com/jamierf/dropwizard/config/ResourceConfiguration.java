package com.jamierf.dropwizard.config;

import com.google.common.base.Optional;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.tools.tar.TarEntry;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

public class ResourceConfiguration {

    @Parameter(required = true)
    private File source;

    @Parameter(required = true)
    private String target;

    @Parameter
    @SuppressWarnings("FieldCanBeLocal")
    private boolean filter = true;

    @Parameter
    @SuppressWarnings("FieldCanBeLocal")
    private String user = null;

    @Parameter
    @SuppressWarnings("FieldCanBeLocal")
    private int mode = TarEntry.DEFAULT_FILE_MODE;

    public File getSource() {
        return checkNotNull(source);
    }

    public String getTarget() {
        return checkNotNull(target);
    }

    public boolean isFilter() {
        return filter;
    }

    public Optional<String> getUser() {
        return Optional.fromNullable(user);
    }

    public int getMode() {
        return mode;
    }
}
