package com.jamierf.dropwizard;

import com.google.common.collect.Collections2;
import com.jamierf.dropwizard.resource.Resource;
import com.jamierf.dropwizard.transforms.ResourceDataProducer;
import com.jamierf.dropwizard.util.LogConsole;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.vafer.jdeb.DataProducer;
import org.vafer.jdeb.DebMaker;
import org.vafer.jdeb.PackagingException;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

public class PackageBuilder {

    private final MavenProject project;
    private final Log log;

    public PackageBuilder(MavenProject project, Log log) {
        this.project = project;
        this.log = log;
    }

    public void createPackage(Collection<Resource> resources, File inputDir, File debFile) throws PackagingException {
        final Collection<DataProducer> dataProducers = Collections2.transform(resources, new ResourceDataProducer(inputDir));
        final DebMaker debMaker = new DebMaker(new LogConsole(log), dataProducers, Collections.<DataProducer>emptySet());

        debMaker.setDeb(debFile);
        debMaker.setSignPackage(false);
        debMaker.setControl(new File(inputDir, "control"));
        debMaker.setPackage(project.getArtifactId());
        debMaker.setDescription(project.getDescription());
        debMaker.setHomepage(project.getUrl());
        debMaker.setCompression("gzip");
        debMaker.validate();
        debMaker.makeDeb();
    }
    
}
