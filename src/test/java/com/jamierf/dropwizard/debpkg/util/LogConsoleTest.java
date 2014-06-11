package com.jamierf.dropwizard.debpkg.util;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.vafer.jdeb.Console;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LogConsoleTest {

    private Log log;
    private Console console;

    @Before
    public void setUp() {
        log = mock(Log.class);
        console = new LogConsole(log);
    }

    @Test
    public void testDebug() {
        console.debug("hello debug");
        verify(log).debug(eq("hello debug"));
    }

    @Test
    public void testInfo() {
        console.info("hello info");
        verify(log).info(eq("hello info"));
    }

    @Test
    public void testWarn() {
        console.warn("hello warn");
        verify(log).warn(eq("hello warn"));
    }
}
