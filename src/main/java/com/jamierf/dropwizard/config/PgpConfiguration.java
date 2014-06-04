package com.jamierf.dropwizard.config;

import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

public class PgpConfiguration {

    @Parameter(required = true)
    private String alias = null;

    @Parameter(required = true)
    private File keyring = null;

    @Parameter(required = true)
    private String passphrase = null;

    @SuppressWarnings("unused")
    public String getAlias() {
        return checkNotNull(alias);
    }

    @SuppressWarnings("unused")
    public File getKeyring() {
        return checkNotNull(keyring);
    }

    @SuppressWarnings("unused")
    public String getPassphrase() {
        return checkNotNull(passphrase);
    }
}
