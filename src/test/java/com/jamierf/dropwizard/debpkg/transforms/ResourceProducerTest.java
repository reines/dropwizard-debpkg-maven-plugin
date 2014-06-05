package com.jamierf.dropwizard.debpkg.transforms;

import com.google.common.io.Files;
import com.jamierf.dropwizard.debpkg.config.ResourceConfiguration;
import com.jamierf.dropwizard.debpkg.resource.Resource;
import com.jamierf.dropwizard.debpkg.transforms.ResourceProducer;
import org.apache.tools.tar.TarEntry;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ResourceProducerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File source;
    private ResourceProducer producer;

    @Before
    public void setUp() throws IOException {
        source = temporaryFolder.newFile();
        Files.write("hello world".getBytes(), source);

        producer = new ResourceProducer("tester");
    }

    @Test
    public void testDefaults() {
        final Resource resource = producer.apply(new ResourceConfiguration().setSource(source).setTarget("/tmp/test"));
        assertNotNull(resource);
        assertEquals("tester", resource.getUser());
        assertEquals(TarEntry.DEFAULT_FILE_MODE, resource.getMode());
        assertTrue(resource.isFilter());
    }

    @Test(expected = NullPointerException.class)
    public void testSourceRequired() {
        producer.apply(new ResourceConfiguration().setTarget("/tmp/test"));
    }

    @Test(expected = NullPointerException.class)
    public void testTargetRequired() {
        producer.apply(new ResourceConfiguration().setSource(source));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSourceFileMustExist() {
        producer.apply(new ResourceConfiguration().setSource(new File("/dsubifjksfldmsf/smdkvjbvhdfk/dsfnjksdhweofp")).setTarget("/tmp/test"));
    }

    @Test
    public void testSource() throws IOException {
        final Resource resource = producer.apply(new ResourceConfiguration().setSource(source).setTarget("/tmp/test"));
        assertNotNull(resource);
        assertArrayEquals(Files.asByteSource(source).read(), resource.getSource().read());
    }

    @Test
    public void testTarget() {
        final Resource resource = producer.apply(new ResourceConfiguration().setSource(source).setTarget("/tmp/test"));
        assertNotNull(resource);
        assertEquals("/tmp/test", resource.getTarget());
    }

    @Test
    public void testUser() {
        final Resource resource = producer.apply(new ResourceConfiguration().setSource(source).setTarget("/tmp/test").setUser("timmy"));
        assertNotNull(resource);
        assertEquals("timmy", resource.getUser());
    }

    @Test
    public void testMode() {
        final Resource resource = producer.apply(new ResourceConfiguration().setSource(source).setTarget("/tmp/test").setMode(0777));
        assertNotNull(resource);
        assertEquals(0777, resource.getMode());
    }

    @Test
    public void testFilter() {
        final Resource resource = producer.apply(new ResourceConfiguration().setSource(source).setTarget("/tmp/test").setFilter(false));
        assertNotNull(resource);
        assertFalse(resource.isFilter());
    }
}
