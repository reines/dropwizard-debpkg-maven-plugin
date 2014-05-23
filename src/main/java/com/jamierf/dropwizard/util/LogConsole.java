package com.jamierf.dropwizard.util;

import org.apache.maven.plugin.logging.Log;
import org.vafer.jdeb.Console;

public class LogConsole implements Console {
    
    private final Log log;

    public LogConsole(Log log) {
        this.log = log;
    }

    @Override
    public void debug(String message) {
        log.debug(message);
    }

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }
}
