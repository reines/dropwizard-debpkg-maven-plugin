package com.jamierf.dropwizard.resource;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;

public class EmbeddedResource extends Resource {

    private final URL resource;

    public EmbeddedResource(String path, boolean filter, String target, String user, String group, int mode) {
        super(filter, target, user, group, mode);

        this.resource = checkNotNull(Resource.class.getResource(path));
    }

    @Override
    public ByteSource getSource() {
        return Resources.asByteSource(resource);
    }
}
