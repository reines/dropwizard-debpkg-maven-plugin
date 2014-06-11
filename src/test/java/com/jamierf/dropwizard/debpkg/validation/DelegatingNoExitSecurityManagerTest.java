package com.jamierf.dropwizard.debpkg.validation;

import org.apache.tools.ant.ExitException;
import org.junit.Before;
import org.junit.Test;

import java.security.Permission;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DelegatingNoExitSecurityManagerTest {

    private SecurityManager delegateSecurityManager;
    private SecurityManager noExitSecurityManager;

    @Before
    public void setUp() {
        delegateSecurityManager = mock(SecurityManager.class);
        noExitSecurityManager = new DelegatingNoExitSecurityManager(delegateSecurityManager);
    }

    @Test(expected = ExitException.class)
    public void testOkExitNotAllowed() {
        noExitSecurityManager.checkExit(0);
    }

    @Test(expected = ExitException.class)
    public void testErrorExitNotAllowed() {
        noExitSecurityManager.checkExit(1);
    }

    @Test
    public void testCheckPermissionDelegates() {
        final Permission perm = mock(Permission.class);
        noExitSecurityManager.checkPermission(perm);
        verify(delegateSecurityManager).checkPermission(eq(perm));
    }

    @Test
    public void testCheckPermissionWithContextDelegates() {
        final Permission perm = mock(Permission.class);
        final Object context = mock(Object.class);

        noExitSecurityManager.checkPermission(perm, context);
        verify(delegateSecurityManager).checkPermission(eq(perm), eq(context));
    }
}
