package com.jamierf.dropwizard.debpkg;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class InvalidDropwizardMojoTest extends AbstractDropwizardMojoTest {

    @Test(expected = NullPointerException.class)
    public void testConfigTemplateRequired() throws Exception {
        final DropwizardMojo plugin = loadPlugin("dwpackage", ConfiguredDropwizardMojoTest.class.getResource("empty.xml"));
        plugin.init();
    }

    @Test(expected = MojoExecutionException.class)
    public void testProjectType() throws Exception {
        final DropwizardMojo plugin = loadPlugin("dwpackage", ConfiguredDropwizardMojoTest.class.getResource("type.xml"));
        plugin.init();
    }
}
