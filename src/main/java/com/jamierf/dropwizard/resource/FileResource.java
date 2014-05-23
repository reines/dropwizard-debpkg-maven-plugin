package com.jamierf.dropwizard.resource;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

public class FileResource extends Resource {
    
    private final File file;

    public FileResource(File file, boolean filter, String target, String user, String group, int mode) {
        super(filter, target, user, group, mode);
        
        this.file = checkNotNull(file);
        Preconditions.checkArgument(file.exists(), String.format("File %s not found", file.getAbsolutePath()));
    }

    @Override
    public ByteSource getSource() {
        return Files.asByteSource(file);
    }
}
