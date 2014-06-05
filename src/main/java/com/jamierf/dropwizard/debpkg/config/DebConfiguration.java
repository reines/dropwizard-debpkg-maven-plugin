package com.jamierf.dropwizard.debpkg.config;

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
    private String maintainer = "Unspecified";

    public String getName() {
        return project.getArtifactId();
    }

    public String getVersion() {
        return Utils.convertToDebianVersion(project.getVersion(), true, "", session.getStartTime());
    }

    public String getMaintainer() {
        return maintainer;
    }

    public void setMaintainer(final String maintainer) {
        this.maintainer = maintainer;
    }
}
