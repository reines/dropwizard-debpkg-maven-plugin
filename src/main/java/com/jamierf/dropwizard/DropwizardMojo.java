package com.jamierf.dropwizard;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import com.jamierf.dropwizard.config.DebConfiguration;
import com.jamierf.dropwizard.config.JvmConfiguration;
import com.jamierf.dropwizard.config.PathConfiguration;
import com.jamierf.dropwizard.config.UnixConfiguration;
import com.jamierf.dropwizard.filter.DependencyFilter;
import com.jamierf.dropwizard.resource.EmbeddedResource;
import com.jamierf.dropwizard.resource.FileResource;
import com.jamierf.dropwizard.resource.Resource;
import com.jamierf.dropwizard.util.LogConsole;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.tools.tar.TarEntry;
import org.codehaus.plexus.util.FileUtils;
import org.vafer.jdeb.Console;
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

    @Parameter
    private File outputFile;

    @Parameter
    private boolean validate = true;

    private Console log = new LogConsole(getLog());

    public void execute() throws MojoExecutionException {
        setupMojoConfiguration();

        final Collection<Resource> resources = buildResourceList();
        final Map<String, Object> parameters = buildParameterMap();

        final File resourcesDir = extractResources(resources, parameters);
        if (validate) {
            validateApplicationConfiguration(resourcesDir);
        }

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

        if (outputFile == null) {
            outputFile = new File(project.getBuild().getDirectory(), String.format("%s-%s.deb", project.getArtifactId(), project.getVersion()));
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
            throw new MojoExecutionException("Failed to extract resources", e);
        }
    }

    private void validateApplicationConfiguration(File resourcesDir) throws MojoExecutionException {
        try {
            final Optional<Dependency> dropwizardDependency = Iterables.tryFind(project.getModel().getDependencies(), new DependencyFilter("io.dropwizard", "dropwizard-core"));
            if (!dropwizardDependency.isPresent()) {
                log.warn("Failed to find Dropwizard dependency in project. Skipping configuration validation.");
                return;
            }

            final ComparableVersion version = new ComparableVersion(dropwizardDependency.get().getVersion());
            log.info(String.format("Detected Dropwizard %s, attempting to validate configuration.", version));

            if (!ApplicationValidator.canSupportVersion(version)) {
                log.warn(String.format("The max Dropwizard version supported by this plugin is %s. If validation fails you can disable this step by setting `validation` to false.",
                        ApplicationValidator.MAX_SUPPORTED_VERSION));
            }

            final File tempDirectory = Files.createTempDir();
            try {
                final File configFile = new File(resourcesDir, "/files" + path.getConfigFile());
                final ApplicationValidator validator = new ApplicationValidator(artifactFile, log, tempDirectory);
                validator.validateConfiguration(configFile);
            }
            finally {
                FileUtils.forceDelete(tempDirectory);
            }
        }
        catch (IOException | IllegalArgumentException | ClassNotFoundException e) {
            throw new MojoExecutionException("Failed to validate configuration", e);
        }
    }

    private File createPackage(Collection<Resource> resources, File inputDir) throws MojoExecutionException {
        try {
            new PackageBuilder(project, log).createPackage(resources, inputDir, outputFile);
            return outputFile;
        }
        catch (PackagingException e) {
            throw new MojoExecutionException("Failed to create Debian package", e);
        }
    }

    private void attachArtifact(File artifact, String type) {
        if (!type.equals(project.getArtifact().getType())) {
            log.info(String.format("Attaching created %s package %s", type, artifact));
            helper.attachArtifact(project, type, artifact);
        } else {
            log.info(String.format("Setting created %s package %s", type, artifact));
            project.getArtifact().setFile(artifact);
        }
    }
}
