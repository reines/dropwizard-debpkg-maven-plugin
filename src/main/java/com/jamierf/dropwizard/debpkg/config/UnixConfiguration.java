package com.jamierf.dropwizard.debpkg.config;

import org.apache.maven.plugins.annotations.Parameter;

public class UnixConfiguration {

    @Parameter
    private String user = "dropwizard";

    @Parameter
    private boolean respawn = true;

    public String getUser() {
        return user;
    }

    public UnixConfiguration setUser(final String user) {
        this.user = user;
        return this;
    }

    public boolean isRespawn() {
        return respawn;
    }

    public UnixConfiguration setRespawn(final boolean respawn) {
        this.respawn = respawn;
        return this;
    }
}
