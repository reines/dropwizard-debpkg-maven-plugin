package com.jamierf.dropwizard;

import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.jamierf.dropwizard.config.PgpConfiguration;
import com.jamierf.dropwizard.resource.Resource;
import com.jamierf.dropwizard.transforms.ResourceDataProducer;
import org.apache.maven.project.MavenProject;
import org.vafer.jdeb.*;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

public class PackageBuilder {

    private static final Compression COMPRESSION = Compression.GZIP;

    private final MavenProject project;
    private final Console log;
    private final Optional<PgpConfiguration> gpgConfiguration;

    public PackageBuilder(final MavenProject project, final Console log, final Optional<PgpConfiguration> gpgConfiguration) {
        this.project = project;
        this.log = log;
        this.gpgConfiguration = gpgConfiguration;
    }

    public void createPackage(final Collection<Resource> resources, final File inputDir, final File debFile) throws PackagingException {
        final Collection<DataProducer> dataProducers = Collections2.transform(resources, new ResourceDataProducer(inputDir));
        final DebMaker debMaker = new DebMaker(log, dataProducers, Collections.<DataProducer>emptySet());

        debMaker.setDeb(debFile);
        debMaker.setControl(new File(inputDir, "control"));

        debMaker.setPackage(project.getArtifactId());
        debMaker.setDescription(project.getDescription());
        debMaker.setHomepage(project.getUrl());
        debMaker.setCompression(COMPRESSION.toString());

        if (gpgConfiguration.isPresent()) {
            final PgpConfiguration gpg = gpgConfiguration.get();
            log.info(String.format("Signing package with GPG key: %s", gpg.getAlias()));

            debMaker.setSignPackage(true);
            debMaker.setKey(gpg.getAlias());
            debMaker.setKeyring(gpg.getKeyring());
            debMaker.setPassphrase(gpg.getPassphrase());
        }
        else {
            log.warn("Creating unsigned package");
        }

        debMaker.validate();
        debMaker.makeDeb();
    }
}
