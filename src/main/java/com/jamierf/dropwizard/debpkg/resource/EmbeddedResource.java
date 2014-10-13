package com.jamierf.dropwizard.debpkg.resource;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final EmbeddedResource that = (EmbeddedResource) o;

        if (resource != null ? !resource.equals(that.resource) : that.resource != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        return result;
    }
}
