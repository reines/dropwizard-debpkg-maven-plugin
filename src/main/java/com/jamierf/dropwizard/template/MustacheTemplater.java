package com.jamierf.dropwizard.template;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.Reader;
import java.io.Writer;

public class MustacheTemplater implements Templater {

    private static final MustacheFactory MUSTACHE = new DefaultMustacheFactory();

    @Override
    public void execute(Reader input, Writer output, String name, Object parameters) {
        final Mustache template = MUSTACHE.compile(input, name);
        // TODO: Error on missing parameters
        template.execute(output, parameters);
    }
}
