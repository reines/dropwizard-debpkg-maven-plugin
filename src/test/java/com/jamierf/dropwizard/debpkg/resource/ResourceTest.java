package com.jamierf.dropwizard.debpkg.resource;

import org.apache.tools.tar.TarEntry;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ResourceTest {

    @Test
    public void testStringResource() throws IOException {
        final Resource resource = new StringResource("string", true, "/tmp/test.txt", "dropwizard", "dropwizard", TarEntry.DEFAULT_FILE_MODE);
        final String result = new String(resource.getSource().read());
        assertEquals("string", result);
    }

    @Test
    public void testEmbeddedResource() throws IOException {
        final Resource resource = new EmbeddedResource("embedded.txt", true, "/tmp/test.txt", "dropwizard", "dropwizard", TarEntry.DEFAULT_FILE_MODE);
        final String result = new String(resource.getSource().read());
        assertEquals("embedded", result);
    }

    @Test
    public void testFileResource() throws IOException {
        final Resource resource = new FileResource(new File(Resource.class.getResource("file.txt").getFile()), true, "/tmp/test.txt", "dropwizard", "dropwizard", TarEntry.DEFAULT_FILE_MODE);
        final String result = new String(resource.getSource().read());
        assertEquals("file", result);
    }
}
