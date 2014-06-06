package com.jamierf.dropwizard.debpkg.validation;

import org.apache.tools.ant.ExitException;

import java.security.Permission;

public class DelegatingNoExitSecurityManager extends SecurityManager {

    private final SecurityManager delegate;

    public DelegatingNoExitSecurityManager(final SecurityManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public void checkExit(final int status) {
        throw new ExitException(status);
    }

    @Override
    public void checkPermission(final Permission perm) {
        if (delegate != null) {
            delegate.checkPermission(perm);
        }
    }

    @Override
    public void checkPermission(final Permission perm, final Object context) {
        if (delegate != null) {
            delegate.checkPermission(perm, context);
        }
    }
}
