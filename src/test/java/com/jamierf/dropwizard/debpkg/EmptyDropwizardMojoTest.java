package com.jamierf.dropwizard.debpkg;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class EmptyDropwizardMojoTest extends AbstractDropwizardMojoTest {

    private DropwizardMojo plugin;

    @Before
    public void setUp() throws Exception {
        plugin = loadPlugin("dwpackage", ConfiguredDropwizardMojoTest.class.getResource("empty.xml"));
    }

    @Test(expected = NullPointerException.class)
    public void testConfigTemplateRequired() throws MojoExecutionException {
        plugin.init();
        assertNull(plugin.configTemplate);
    }
}
