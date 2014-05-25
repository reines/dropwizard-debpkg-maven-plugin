package com.jamierf.dropwizard.resource;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;

public class EmbeddedResource extends Resource {

    private final URL resource;

    public EmbeddedResource(final String path, final boolean filter, final String target, final String user, final String group, final int mode) {
        super(filter, target, user, group, mode);

        this.resource = checkNotNull(Resource.class.getResource(path));
    }

    @Override
    public ByteSource getSource() {
        return Resources.asByteSource(resource);
    }
}
