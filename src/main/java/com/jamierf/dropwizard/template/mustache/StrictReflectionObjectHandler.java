package com.jamierf.dropwizard.template.mustache;

import com.github.mustachejava.reflect.Guard;
import com.github.mustachejava.reflect.MissingWrapper;
import com.github.mustachejava.reflect.ReflectionObjectHandler;
import com.jamierf.dropwizard.template.MissingParameterException;

import java.util.List;

public class StrictReflectionObjectHandler extends ReflectionObjectHandler {
    @Override
    protected MissingWrapper createMissingWrapper(final String name, final List<Guard> guards) {
        throw new MissingParameterException(name);
    }
}
