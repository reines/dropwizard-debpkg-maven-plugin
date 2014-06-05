package com.jamierf.dropwizard.debpkg.util;

import org.vafer.jdeb.Console;

public class SystemConsole implements Console {
    @Override
    public void debug(final String message) {
        System.out.println(message);
    }

    @Override
    public void info(final String message) {
        System.out.println(message);
    }

    @Override
    public void warn(final String message) {
        System.err.println(message);
    }
}
