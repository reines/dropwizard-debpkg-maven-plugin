package com.jamierf.dropwizard.debpkg.filter;

import com.jamierf.dropwizard.debpkg.filter.DependencyFilter;
import org.apache.maven.model.Dependency;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DependencyFilterTest {

    private static Dependency createDependency(String groupId, String artifactId, String version) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        return dependency;
    }

    private DependencyFilter filter;

    @Before
    public void setUp() {
        filter = new DependencyFilter("com.jamierf", "test");
    }

    @Test
    public void testMatchingArtifactPasses() {
        assertTrue(filter.apply(createDependency("com.jamierf", "test", "1.0")));
    }

    @Test
    public void testVersionIgnored() {
        assertTrue(filter.apply(createDependency("com.jamierf", "test", "1.0")));
        assertTrue(filter.apply(createDependency("com.jamierf", "test", "1.1")));
        assertTrue(filter.apply(createDependency("com.jamierf", "test", "0.0.1-SNAPSHOT")));
        assertTrue(filter.apply(createDependency("com.jamierf", "test", "1.0-alpha")));
    }

    @Test
    public void testNotMatchingGroupIdRejected() {
        assertFalse(filter.apply(createDependency("org.example", "test", "1.0")));
    }

    @Test
    public void testNotMatchingArtifactIdRejected() {
        assertFalse(filter.apply(createDependency("com.jamierf", "example", "1.0")));
    }
}
