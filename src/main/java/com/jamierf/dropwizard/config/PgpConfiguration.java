package com.jamierf.dropwizard.config;

import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public class PgpConfiguration {

    @Parameter
    private String alias = null;

    @Parameter
    private File keyring = null;

    @Parameter
    private String passphrase = null;

    @SuppressWarnings("unused")
    public String getAlias() {
        return alias;
    }

    @SuppressWarnings("unused")
    public File getKeyring() {
        return keyring;
    }

    @SuppressWarnings("unused")
    public String getPassphrase() {
        return passphrase;
    }
}
