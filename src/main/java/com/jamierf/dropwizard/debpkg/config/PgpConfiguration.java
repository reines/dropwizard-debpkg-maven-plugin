package com.jamierf.dropwizard.debpkg.config;

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

    public String getAlias() {
        return checkNotNull(alias);
    }

    public PgpConfiguration setAlias(final String alias) {
        this.alias = alias;
        return this;
    }

    public File getKeyring() {
        return checkNotNull(keyring);
    }

    public PgpConfiguration setKeyring(final File keyring) {
        this.keyring = keyring;
        return this;
    }

    public String getPassphrase() {
        return checkNotNull(passphrase);
    }

    public PgpConfiguration setPassphrase(final String passphrase) {
        this.passphrase = passphrase;
        return this;
    }
}
