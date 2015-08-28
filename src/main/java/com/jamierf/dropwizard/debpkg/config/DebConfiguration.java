package com.jamierf.dropwizard.debpkg.config;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.vafer.jdeb.utils.Utils;

import java.util.Date;

import com.google.common.base.Strings;

public class DebConfiguration {

    private static final Date TIMESTAMP = new Date();

    private MavenProject project;

    public DebConfiguration setProject(final MavenProject project) {
        this.project = project;
        return this;
    }

    @Parameter
    private String maintainer = "Unspecified";

    @Parameter
    private String name = null;

    @Parameter
    private String version = null;

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return project.getArtifactId();
        }
    }

    public String getVersion() {
        return Strings.isNullOrEmpty(version) ? Utils.convertToDebianVersion(
            project.getVersion(), true, "", TIMESTAMP) : version;
    }

    public String getMaintainer() {
        return maintainer;
    }

    public DebConfiguration setMaintainer(final String maintainer) {
        this.maintainer = maintainer;
        return this;
    }

    public DebConfiguration setName(final String name) {
        this.name = name;
        return this;
    }

    public DebConfiguration setVersion(final String version) {
        this.version = version;
        return this;
    }
}
