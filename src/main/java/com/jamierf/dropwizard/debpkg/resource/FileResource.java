package com.jamierf.dropwizard.debpkg.resource;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

public class FileResource extends Resource {
    
    private final File file;

    public FileResource(final File file, final boolean filter, final String target, final String user, final String group, final int mode) {
        super(filter, target, user, group, mode);
        
        this.file = checkNotNull(file);
        Preconditions.checkArgument(file.exists(), String.format("File %s not found", file.getAbsolutePath()));
    }

    @Override
    public ByteSource getSource() {
        return Files.asByteSource(file);
    }
}
