package com.jamierf.dropwizard.debpkg;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.jamierf.dropwizard.debpkg.resource.Resource;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

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
        assertTrue(plugin.unix.isRespawn());
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
        assertEquals("/etc/init.d/test", plugin.path.getSystemVFile());
        assertEquals("/lib/systemd/system/test.service", plugin.path.getSystemDFile());
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

    @Test
    public void testParameterMapContainsExpectedValues() {
        final Map<String, Object> params = plugin.buildParameterMap();
        assertEquals(plugin.project, params.get("project"));
        assertEquals(plugin.deb, params.get("deb"));
        assertEquals(plugin.jvm, params.get("jvm"));
        assertEquals(plugin.unix, params.get("unix"));
        assertEquals(plugin.dropwizard, params.get("dw"));
        assertEquals(plugin.dropwizard, params.get("dropwizard"));
        assertEquals(plugin.path, params.get("path"));
    }

    @Test
    public void testResourceListContainsExpectedResources() {
        final Set<String> targets = ImmutableSet.copyOf(Collections2.transform(plugin.buildResourceList(), new Function<Resource, String>() {
            @Nullable
            @Override
            public String apply(final Resource input) {
                return input.getTarget();
            }
        }));

        assertTrue(targets.contains(plugin.path.getConfigFile()));
        assertTrue(targets.contains(plugin.path.getJvmConfigFile()));
        assertTrue(targets.contains(plugin.path.getUpstartFile()));
        assertTrue(targets.contains(plugin.path.getSystemVFile()));
        assertTrue(targets.contains(plugin.path.getSystemDFile()));
        assertTrue(targets.contains(plugin.path.getStartScript()));
        assertTrue(targets.contains(plugin.path.getJarFile()));
    }
}
