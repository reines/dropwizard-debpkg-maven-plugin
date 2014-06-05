package com.jamierf.dropwizard.debpkg.util;

import org.apache.maven.plugin.logging.Log;
import org.vafer.jdeb.Console;

public class LogConsole implements Console {
    
    private final Log log;

    public LogConsole(final Log log) {
        this.log = log;
    }

    @Override
    public void debug(final String message) {
        log.debug(message);
    }

    @Override
    public void info(final String message) {
        log.info(message);
    }

    @Override
    public void warn(final String message) {
        log.warn(message);
    }
}
