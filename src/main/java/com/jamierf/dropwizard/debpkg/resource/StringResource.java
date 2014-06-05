package com.jamierf.dropwizard.debpkg.resource;

import com.google.common.io.ByteSource;

import java.nio.charset.StandardCharsets;

public class StringResource extends Resource {

    private final byte[] source;

    public StringResource(final String source, final boolean filter, final String target, final String user, final String group, final int mode) {
        super(filter, target, user, group, mode);

        this.source = source.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ByteSource getSource() {
        return ByteSource.wrap(source);
    }
}
