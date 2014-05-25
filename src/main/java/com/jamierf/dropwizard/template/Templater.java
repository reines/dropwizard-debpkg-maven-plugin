package com.jamierf.dropwizard.template;

import com.jamierf.dropwizard.template.mustache.MustacheTemplater;

import java.io.*;

public abstract class Templater {

    public static Templater getDefault() {
        return new MustacheTemplater();
    }

    public abstract void execute(final Reader input, final Writer output, final String name, final Object parameters);

    public String execute(final String input, final String name, final Object parameters) throws IOException {
        if (input == null) {
            return null;
        }

        try (final Reader reader = new StringReader(input)) {
            final StringWriter writer = new StringWriter();
            execute(reader, writer, name, parameters);
            return writer.toString();
        }
    }
}
