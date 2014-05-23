package com.jamierf.dropwizard.resource;

import com.google.common.io.ByteSource;

public abstract class Resource {

    private final boolean filter;
    private final String target;
    private final String user;
    private final String group;
    private final int mode;

    public Resource(boolean filter, String target, String user, String group, int mode) {
        this.target = target;
        this.user = user;
        this.group = group;
        this.filter = filter;
        this.mode = mode;
    }

    public abstract ByteSource getSource();

    public boolean isFilter() {
        return filter;
    }

    public String getTarget() {
        return target;
    }

    public String getUser() {
        return user;
    }

    public String getGroup() {
        return group;
    }

    public int getMode() {
        return mode;
    }
}
