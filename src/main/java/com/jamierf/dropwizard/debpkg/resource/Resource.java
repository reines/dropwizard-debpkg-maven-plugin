package com.jamierf.dropwizard.debpkg.resource;

import com.google.common.io.ByteSource;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Resource {

    private final boolean filter;
    private final String target;
    private final String user;
    private final String group;
    private final int mode;

    public Resource(final boolean filter, final String target, final String user, final String group, final int mode) {
        this.target = checkNotNull(target);
        this.user = checkNotNull(user);
        this.group = checkNotNull(group);
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
