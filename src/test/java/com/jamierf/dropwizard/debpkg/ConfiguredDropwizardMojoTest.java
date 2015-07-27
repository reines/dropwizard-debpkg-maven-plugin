package com.jamierf.dropwizard.debpkg;

import com.jamierf.dropwizard.debpkg.config.ResourceConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfiguredDropwizardMojoTest extends AbstractDropwizardMojoTest {

    private DropwizardMojo plugin;

    @Before
    public void setUp() throws Exception {
        plugin = loadPlugin("dwpackage", ConfiguredDropwizardMojoTest.class.getResource("configured.xml"));
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
        assertEquals("Jamie Furness", plugin.deb.getMaintainer());
        assertEquals("AlternativePackageName", plugin.deb.getName());
        assertEquals("1.0", plugin.deb.getVersion());
    }

    @Test
    public void testJvmParameter() {
        assertNotNull(plugin.jvm);
        assertEquals("256m", plugin.jvm.getMemory());
        assertEquals("oracle-java8-installer", plugin.jvm.getPackageName());
        assertEquals("8u20+8u6arm-1~webupd8~0", plugin.jvm.getPackageVersion());
        assertFalse(plugin.jvm.isServer());
    }

    @Test
    public void testUnixParameter() {
        assertNotNull(plugin.unix);
        assertEquals("test", plugin.unix.getUser());
        assertFalse(plugin.unix.isRespawn());
    }

    @Test
    public void testPathParameter() {
        assertNotNull(plugin.path);
        assertEquals("/tmp/test.jar", plugin.path.getJarFile());
        assertEquals("/tmp/start.sh", plugin.path.getStartScript());
        assertEquals("/tmp/config.yml", plugin.path.getConfigFile());
        assertEquals("/tmp/jvm.conf", plugin.path.getJvmConfigFile());
        assertEquals("/tmp/log", plugin.path.getLogDirectory());
        assertEquals("/tmp/upstart.conf", plugin.path.getUpstartFile());
        assertEquals("/tmp/init.sh", plugin.path.getSystemVFile());
        assertEquals("/tmp/systemd.service", plugin.path.getSystemDFile());
    }

    @Test
    public void testDropwizardParameter() {
        assertNotNull(plugin.dropwizard);
        assertEquals(3, plugin.dropwizard.size());
        assertEquals("8080", plugin.dropwizard.get("httpPort"));
        assertEquals("8081", plugin.dropwizard.get("httpAdminPort"));
        assertEquals("This is a test", plugin.dropwizard.get("message"));
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
        assertNotNull(plugin.pgp);
        assertEquals("test", plugin.pgp.getAlias());
        assertEquals("test.jks", plugin.pgp.getKeyring().getName());
        assertEquals("qwerty", plugin.pgp.getPassphrase());
    }

    @Test
    public void testValidateParameter() {
        assertTrue(plugin.validate);
    }

    @Test
    @SuppressWarnings("OctalInteger")
    public void testFilesParameter() {
        assertNotNull(plugin.files);
        assertEquals(1, plugin.files.size());

        final ResourceConfiguration resource = plugin.files.get(0);
        assertEquals("test.gz", resource.getSource().getName());
        assertEquals("/tmp/test.gz", resource.getTarget());
        assertFalse(resource.isFilter());
        assertTrue(resource.getUser().isPresent());
        assertEquals("test", resource.getUser().get());
        assertEquals(0600, resource.getMode());
    }
}
