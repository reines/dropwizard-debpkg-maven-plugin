package com.jamierf.dropwizard.config;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public class PathConfiguration {
    
    private MavenProject project;

    public void setProject(MavenProject project) {
        this.project = project;
    }

    @Parameter
    private String jarFile = null;
    
    @Parameter
    private String configFile = null;
    
    @Parameter
    private String logDirectory = null;

    @Parameter
    private String upstartFile = null;

    @SuppressWarnings("unused")
    public String getJarFile() {
        return jarFile == null ? String.format("/usr/share/java/%s.jar", project.getArtifactId()) : jarFile;
    }

    @SuppressWarnings("unused")
    public String getConfigFile() {
        return configFile == null ? String.format("/etc/%s.yml", project.getArtifactId()) : configFile;
    }

    @SuppressWarnings("unused")
    public String getLogDirectory() {
        return logDirectory == null ? String.format("/var/log/%s", project.getArtifactId()) : logDirectory;
    }

    @SuppressWarnings("unused")
    public String getUpstartFile() {
        return upstartFile == null ? String.format("/etc/init/%s.conf", project.getArtifactId()) : upstartFile;
    }
}
