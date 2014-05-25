package com.jamierf.dropwizard.config;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.vafer.jdeb.utils.Utils;

public class DebConfiguration {

    private MavenProject project;
    private MavenSession session;

    public void setProject(final MavenProject project) {
        this.project = project;
    }

    public void setSession(final MavenSession session) {
        this.session = session;
    }

    @Parameter
    @SuppressWarnings("FieldCanBeLocal")
    private final String maintainer = "Unspecified";

    @SuppressWarnings("unused")
    public String getName() {
        return project.getArtifactId();
    }

    @SuppressWarnings("unused")
    public String getVersion() {
        return Utils.convertToDebianVersion(project.getVersion(), true, "", session.getStartTime());
    }

    @SuppressWarnings("unused")
    public String getMaintainer() {
        return maintainer;
    }
}
