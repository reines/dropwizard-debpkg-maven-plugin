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
    private String startScript = null;

    @Parameter
    private String configFile = null;

    @Parameter
    private String jvmConfigFile = null;

    @Parameter
    private String logDirectory = null;

    @Parameter
    private String upstartFile = null;

    @Parameter
    private String sysVinitFile = null;

    public String getJarFile() {
        return jarFile == null ? String.format("/usr/share/java/%s.jar", project.getArtifactId()) : jarFile;
    }

    public PathConfiguration setJarFile(final String jarFile) {
        this.jarFile = jarFile;
        return this;
    }

    public String getStartScript() {
        return startScript == null ? String.format("/usr/bin/%s", project.getArtifactId()) : startScript;
    }

    public PathConfiguration setStartScript(final String startScript) {
        this.startScript = startScript;
        return this;
    }

    public String getConfigFile() {
        return configFile == null ? String.format("/etc/%s.yml", project.getArtifactId()) : configFile;
    }

    public PathConfiguration setConfigFile(final String configFile) {
        this.configFile = configFile;
        return this;
    }

    public String getJvmConfigFile() {
        return jvmConfigFile == null ? String.format("/etc/%s.jvm.conf", project.getArtifactId()) : jvmConfigFile;
    }

    public PathConfiguration setJvmConfigFile(final String jvmConfigFile) {
        this.jvmConfigFile = jvmConfigFile;
        return this;
    }

    public String getLogDirectory() {
        return logDirectory == null ? String.format("/var/log/%s", project.getArtifactId()) : logDirectory;
    }

    public PathConfiguration setLogDirectory(final String logDirectory) {
        this.logDirectory = logDirectory;
        return this;
    }

    public String getUpstartFile() {
        return upstartFile == null ? String.format("/etc/init/%s.conf", project.getArtifactId()) : upstartFile;
    }

    public PathConfiguration setUpstartFile(final String upstartFile) {
        this.upstartFile = upstartFile;
        return this;
    }

    public String getSysVinitFile() {
        return sysVinitFile == null ? String.format("/etc/init.d/%s", project.getArtifactId()) : sysVinitFile;
    }

    public PathConfiguration setSysVinitFile(final String sysVinitFile) {
        this.sysVinitFile = sysVinitFile;
        return this;
    }
}
