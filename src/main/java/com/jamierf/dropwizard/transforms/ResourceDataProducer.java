package com.jamierf.dropwizard.transforms;

import com.google.common.base.Function;
import com.jamierf.dropwizard.resource.Resource;
import org.vafer.jdeb.DataConsumer;
import org.vafer.jdeb.DataProducer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceDataProducer implements Function<Resource, DataProducer> {
    
    private final File inputDir;

    public ResourceDataProducer(File inputDir) {
        this.inputDir = inputDir;
    }

    @Override
    public DataProducer apply(final Resource resource) {
        final File source = new File(inputDir, "files" + resource.getTarget());
        return new DataProducer() {
            @Override
            public void produce(DataConsumer receiver) throws IOException {
                try (final InputStream in = new FileInputStream(source)) {
                    receiver.onEachFile(
                            in,
                            resource.getTarget(),
                            "",
                            resource.getUser(), 0,
                            resource.getGroup(), 0,
                            resource.getMode(),
                            source.length()
                    );
                }
            }
        };
    }
}
