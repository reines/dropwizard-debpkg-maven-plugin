package com.jamierf.dropwizard.template.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.google.common.base.Throwables;
import com.jamierf.dropwizard.template.MissingParameterException;
import com.jamierf.dropwizard.template.Templater;

import java.io.Reader;
import java.io.Writer;

public class MustacheTemplater extends Templater {

    private static final DefaultMustacheFactory MUSTACHE = new DefaultMustacheFactory();

    static {
        MUSTACHE.setObjectHandler(new StrictReflectionObjectHandler());
    }

    @Override
    public void execute(Reader input, Writer output, String name, Object parameters) {
        try {
            final Mustache template = MUSTACHE.compile(input, name);
            template.execute(output, parameters);
        }
        catch (MustacheException e) {
            final Throwable cause = Throwables.getRootCause(e);
            Throwables.propagateIfInstanceOf(cause, MissingParameterException.class);
            throw e;
        }
    }
}
