package com.jamierf.dropwizard;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jamierf.dropwizard.config.DebConfiguration;
import com.jamierf.dropwizard.config.JvmConfiguration;
import com.jamierf.dropwizard.config.PathConfiguration;
import com.jamierf.dropwizard.config.UnixConfiguration;
import com.jamierf.dropwizard.resource.EmbeddedResource;
import com.jamierf.dropwizard.resource.FileResource;
import com.jamierf.dropwizard.resource.Resource;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.tools.tar.TarEntry;
import org.vafer.jdeb.PackagingException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings("unused")
@Mojo(name = "dwpackage", defaultPhase = LifecyclePhase.PACKAGE)
public class DropwizardMojo extends AbstractMojo {

    @Component
    private MavenProjectHelper helper;

    @Component
    private MavenProject project;

    @Component
    private MavenSession session;
    
    @Parameter
    private DebConfiguration deb = new DebConfiguration();

    @Parameter
    private JvmConfiguration jvm = new JvmConfiguration();

    @Parameter
    private UnixConfiguration unix = new UnixConfiguration();

    @Parameter
    private PathConfiguration path = new PathConfiguration();

    @Parameter
    private Map<String, String> dropwizard;

    @Parameter(required = true)
    private File configTemplate;

    @Parameter
    private File artifactFile;
    
    public void execute() throws MojoExecutionException {
        setupMojoConfiguration();

        final Collection<Resource> resources = buildResourceList();
        final Map<String, Object> parameters = buildParameterMap();

        final File resourcesDir = extractResources(resources, parameters);
        final File debFile = createPackage(resources, resourcesDir);

        attachArtifact(debFile, "deb");
    }

    private void setupMojoConfiguration() {
        deb.setProject(project);
        deb.setSession(session);
        path.setProject(project);

        if (artifactFile == null) {
            // TODO: This probably isn't the best way to find the right artifact (what if the project has <packaging>deb</packaging>?)
            artifactFile = project.getArtifact().getFile();
        }
    }

    private Collection<Resource> buildResourceList() {
        return ImmutableList.<Resource>builder()
                .add(new FileResource(configTemplate, true, path.getConfigFile(), unix.getUser(), unix.getUser(), 0100600))
                .add(new EmbeddedResource("/files/upstart.conf", true, path.getUpstartFile(), "root", "root", TarEntry.DEFAULT_FILE_MODE))
                .add(new FileResource(artifactFile, false, path.getJarFile(), unix.getUser(), unix.getUser(), TarEntry.DEFAULT_FILE_MODE))
                .build();
    }

    private Map<String, Object> buildParameterMap() {
        return ImmutableMap.<String, Object>builder()
                .put("project", project)
                .put("session", session)
                .put("deb", deb)
                .put("jvm", jvm)
                .put("unix", unix)
                .put("dw", dropwizard)
                .put("dropwizard", dropwizard)
                .put("path", path)
                .build();
    }

    private File extractResources(Collection<Resource> resources, Map<String, Object> parameters) throws MojoExecutionException {
        try {
            final File outputDir = new File(project.getBuild().getDirectory(), "dropwizard-package");
            new ResourceExtractor(parameters, getLog()).extractResources(resources, outputDir);
            return outputDir;
        }
        catch (IOException e) {
            getLog().error(e);
            throw new MojoExecutionException("Failed to extract resources", e);
        }
    }

    private File createPackage(Collection<Resource> resources, File inputDir) throws MojoExecutionException {
        try {
            final File debFile = new File(project.getBuild().getDirectory(), String.format("%s-%s.deb", project.getArtifactId(), project.getVersion()));
            new PackageBuilder(project, getLog()).createPackage(resources, inputDir, debFile);
            return debFile;
        }
        catch (PackagingException e) {
            getLog().error("Failed to create Debian package", e);
            throw new MojoExecutionException("Failed to create Debian package", e);
        }
    }

    private void attachArtifact(File artifact, String type) {
        getLog().info(String.format("Attaching created %s package %s", type, artifact));
        if (!type.equals(project.getArtifact().getType())) {
            helper.attachArtifact(project, type, artifact);
        } else {
            project.getArtifact().setFile(artifact);
        }
    }
}
