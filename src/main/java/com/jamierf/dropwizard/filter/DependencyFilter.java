package com.jamierf.dropwizard.filter;

import com.google.common.base.Predicate;
import org.apache.maven.model.Dependency;

public class DependencyFilter implements Predicate<Dependency> {

    private final StringMatchingFilter groupFilter;
    private final StringMatchingFilter artifactFilter;

    public DependencyFilter(final String groupId, final String artifactId) {
        groupFilter = new StringMatchingFilter(groupId);
        artifactFilter = new StringMatchingFilter(artifactId);
    }

    @Override
    public boolean apply(final Dependency dependency) {
        return dependency != null
                && groupFilter.apply(dependency.getGroupId())
                && artifactFilter.apply(dependency.getArtifactId());
    }
}
