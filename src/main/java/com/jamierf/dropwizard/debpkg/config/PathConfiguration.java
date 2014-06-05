package com.jamierf.dropwizard.debpkg.config;

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
    private String jvmConfigFile = null;

    @Parameter
    private String logDirectory = null;

    @Parameter
    private String upstartFile = null;

    public String getJarFile() {
        return jarFile == null ? String.format("/usr/share/java/%s.jar", project.getArtifactId()) : jarFile;
    }

    public void setJarFile(final String jarFile) {
        this.jarFile = jarFile;
    }

    public String getConfigFile() {
        return configFile == null ? String.format("/etc/%s.yml", project.getArtifactId()) : configFile;
    }

    public void setConfigFile(final String configFile) {
        this.configFile = configFile;
    }

    public String getJvmConfigFile() {
        return jvmConfigFile == null ? String.format("/etc/%s.jvm.conf", project.getArtifactId()) : jvmConfigFile;
    }

    public void setJvmConfigFile(final String jvmConfigFile) {
        this.jvmConfigFile = jvmConfigFile;
    }

    public String getLogDirectory() {
        return logDirectory == null ? String.format("/var/log/%s", project.getArtifactId()) : logDirectory;
    }

    public void setLogDirectory(final String logDirectory) {
        this.logDirectory = logDirectory;
    }

    public String getUpstartFile() {
        return upstartFile == null ? String.format("/etc/init/%s.conf", project.getArtifactId()) : upstartFile;
    }

    public void setUpstartFile(final String upstartFile) {
        this.upstartFile = upstartFile;
    }
}
