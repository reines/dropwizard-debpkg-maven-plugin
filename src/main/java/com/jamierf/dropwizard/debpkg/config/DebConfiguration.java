package com.jamierf.dropwizard.debpkg.config;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.vafer.jdeb.utils.Utils;

import java.util.Date;

public class DebConfiguration {

    private MavenProject project;

    public DebConfiguration setProject(final MavenProject project) {
        this.project = project;
        return this;
    }

    @Parameter
    private String maintainer = "Unspecified";

    @Parameter
    private String name = null;

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return project.getArtifactId();
        }
    }

    public String getVersion() {
        return Utils.convertToDebianVersion(project.getVersion(), true, "", new Date());
    }

    public String getMaintainer() {
        return maintainer;
    }

    public DebConfiguration setMaintainer(final String maintainer) {
        this.maintainer = maintainer;
        return this;
    }
    
    public DebConfiguration setName(String name) {
		this.name = name;
		return this;
	}
}
