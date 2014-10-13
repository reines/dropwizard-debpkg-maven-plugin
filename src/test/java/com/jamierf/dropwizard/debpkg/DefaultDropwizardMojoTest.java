package com.jamierf.dropwizard.debpkg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultDropwizardMojoTest extends AbstractDropwizardMojoTest {

    private DropwizardMojo plugin;

    @Before
    public void setUp() throws Exception {
        plugin = loadPlugin("dwpackage", DefaultDropwizardMojoTest.class.getResource("default.xml"));
        plugin.init();
    }

    @Test
    public void testHelperComponent() {
        assertNotNull(plugin.helper);
    }

    @Test
    public void testProjectComponent() {
        assertNotNull(plugin.project);
    }

    @Test
    public void testDebParameter() {
        assertNotNull(plugin.deb);
        assertEquals("Unspecified", plugin.deb.getMaintainer());
        assertEquals("test", plugin.deb.getName());
        assertEquals("1.0", plugin.deb.getVersion());
    }

    @Test
    public void testJvmParameter() {
        assertNotNull(plugin.jvm);
        assertEquals("128m", plugin.jvm.getMemory());
        assertEquals("openjdk-7-jdk", plugin.jvm.getPackageName());
        assertNull(plugin.jvm.getPackageVersion());
        assertTrue(plugin.jvm.isServer());
    }

    @Test
    public void testUnixParameter() {
        assertNotNull(plugin.unix);
        assertEquals("dropwizard", plugin.unix.getUser());
    }

    @Test
    public void testPathParameter() {
        assertNotNull(plugin.path);
        assertEquals("/usr/share/java/test.jar", plugin.path.getJarFile());
        assertEquals("/usr/bin/test", plugin.path.getStartScript());
        assertEquals("/etc/test.yml", plugin.path.getConfigFile());
        assertEquals("/etc/test.jvm.conf", plugin.path.getJvmConfigFile());
        assertEquals("/var/log/test", plugin.path.getLogDirectory());
        assertEquals("/etc/init/test.conf", plugin.path.getUpstartFile());
        assertEquals("/etc/init.d/test", plugin.path.getSysVinitFile());
    }

    @Test
    public void testDropwizardParameter() {
        assertNotNull(plugin.dropwizard);
        assertTrue(plugin.dropwizard.isEmpty());
    }

    @Test
    public void testConfigTemplateParameter() {
        assertNotNull(plugin.configTemplate);
        assertEquals("test.yml", plugin.configTemplate.getName());
    }

    @Test
    public void testArtifactFileParameter() {
        assertNotNull(plugin.artifactFile);
    }

    @Test
    public void testOutputFileParameter() {
        assertNotNull(plugin.outputFile);
    }

    @Test
    public void testPgpParameter() {
        assertNull(plugin.pgp);
    }

    @Test
    public void testValidateParameter() {
        assertTrue(plugin.validate);
    }

    @Test
    public void testFilesParameter() {
        assertNotNull(plugin.files);
        assertTrue(plugin.files.isEmpty());
    }
}
