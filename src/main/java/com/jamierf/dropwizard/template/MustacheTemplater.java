package com.jamierf.dropwizard.template;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.Reader;
import java.io.Writer;
import java.util.UUID;

public class MustacheTemplater implements Templater {

    private static final MustacheFactory MUSTACHE = new DefaultMustacheFactory();

    private static String randomTemplateName() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void execute(Reader input, Writer output, Object parameters) {
        final Mustache template = MUSTACHE.compile(input, randomTemplateName());
        // TODO: Error on missing parameters
        template.execute(output, parameters);
    }
}
