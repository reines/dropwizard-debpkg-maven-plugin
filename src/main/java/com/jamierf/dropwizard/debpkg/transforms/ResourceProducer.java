package com.jamierf.dropwizard.debpkg.transforms;

import com.google.common.base.Function;
import com.jamierf.dropwizard.debpkg.config.ResourceConfiguration;
import com.jamierf.dropwizard.debpkg.resource.FileResource;
import com.jamierf.dropwizard.debpkg.resource.Resource;

public class ResourceProducer implements Function<ResourceConfiguration, Resource> {

    private final String defaultUser;

    public ResourceProducer(final String defaultUser) {
        this.defaultUser = defaultUser;
    }

    @Override
    public Resource apply(final ResourceConfiguration input) {
        if (input == null) {
            return null;
        }

        final String user = input.getUser().or(defaultUser);
        return new FileResource(input.getSource(), input.isFilter(), input.getTarget(), user, user, input.getMode());
    }
}
