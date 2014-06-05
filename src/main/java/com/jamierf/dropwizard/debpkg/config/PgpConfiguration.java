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

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public File getKeyring() {
        return checkNotNull(keyring);
    }

    public void setKeyring(final File keyring) {
        this.keyring = keyring;
    }

    public String getPassphrase() {
        return checkNotNull(passphrase);
    }

    public void setPassphrase(final String passphrase) {
        this.passphrase = passphrase;
    }
}
