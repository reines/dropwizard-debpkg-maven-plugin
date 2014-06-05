package com.jamierf.dropwizard.debpkg.config;

import com.google.common.base.Optional;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.tools.tar.TarEntry;

import java.io.File;

public class ResourceConfiguration {

    @Parameter(required = true)
    private File source;

    @Parameter(required = true)
    private String target;

    @Parameter
    private boolean filter = true;

    @Parameter
    private String user = null;

    @Parameter
    private int mode = TarEntry.DEFAULT_FILE_MODE;

    public File getSource() {
        return source;
    }

    public ResourceConfiguration setSource(final File source) {
        this.source = source;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public ResourceConfiguration setTarget(final String target) {
        this.target = target;
        return this;
    }

    public boolean isFilter() {
        return filter;
    }

    public ResourceConfiguration setFilter(final boolean filter) {
        this.filter = filter;
        return this;
    }

    public Optional<String> getUser() {
        return Optional.fromNullable(user);
    }

    public ResourceConfiguration setUser(final String user) {
        this.user = user;
        return this;
    }

    public int getMode() {
        return mode;
    }

    public ResourceConfiguration setMode(final int mode) {
        this.mode = mode;
        return this;
    }
}
