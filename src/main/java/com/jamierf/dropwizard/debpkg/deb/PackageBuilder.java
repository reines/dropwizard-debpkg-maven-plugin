package com.jamierf.dropwizard.debpkg.deb;

import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.jamierf.dropwizard.debpkg.config.PgpConfiguration;
import com.jamierf.dropwizard.debpkg.resource.Resource;
import com.jamierf.dropwizard.debpkg.transforms.ResourceDataProducer;
import org.vafer.jdeb.*;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

public class PackageBuilder {

    private static final Compression COMPRESSION = Compression.GZIP;

    private final String title;
    private final String description;
    private final URI homepage;
    private final Console log;
    private final Optional<PgpConfiguration> pgpConfiguration;

    public PackageBuilder(final String title, final String description, final URI homepage, final Console log, final Optional<PgpConfiguration> pgpConfiguration) {
        this.title = title;
        this.description = description;
        this.homepage = homepage;
        this.log = log;
        this.pgpConfiguration = pgpConfiguration;
    }

    public void createPackage(final Collection<Resource> resources, final File inputDir, final File debFile) throws PackagingException {
        final Collection<DataProducer> dataProducers = Collections2.transform(resources, new ResourceDataProducer(inputDir));
        final DebMaker debMaker = new DebMaker(log, dataProducers, Collections.<DataProducer>emptySet());

        debMaker.setDeb(debFile);
        debMaker.setControl(new File(inputDir, "control"));

        debMaker.setPackage(title);
        debMaker.setDescription(description);
        debMaker.setHomepage(homepage.toString());
        debMaker.setCompression(COMPRESSION.toString());

        if (pgpConfiguration.isPresent()) {
            final PgpConfiguration pgp = pgpConfiguration.get();
            log.info(String.format("Signing package with PGP key: %s", pgp.getAlias()));

            debMaker.setSignPackage(true);
            debMaker.setKey(pgp.getAlias());
            debMaker.setKeyring(pgp.getKeyring());
            debMaker.setPassphrase(pgp.getPassphrase());
        }
        else {
            log.warn("Creating unsigned package");
        }

        debMaker.validate();
        debMaker.makeDeb();
    }
}
