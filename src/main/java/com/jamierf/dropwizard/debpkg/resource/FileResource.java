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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final FileResource that = (FileResource) o;

        if (file != null ? !file.equals(that.file) : that.file != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (file != null ? file.hashCode() : 0);
        return result;
    }
}
