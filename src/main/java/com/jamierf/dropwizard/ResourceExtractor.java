package com.jamierf.dropwizard;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.jamierf.dropwizard.resource.Resource;
import com.jamierf.dropwizard.template.MustacheTemplater;
import com.jamierf.dropwizard.template.Templater;
import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class ResourceExtractor {

    private static final Templater TEMPLATER = new MustacheTemplater();
    private static final Collection<String> CONTROL_RESOURCES = ImmutableSet.of(
            "control", "preinst", "postinst", "prerm", "postrm"
    );

    private final Map<String, Object> parameters;
    private final Log log;

    public ResourceExtractor(Map<String, Object> parameters, Log log) {
        this.parameters = parameters;
        this.log = log;
    }

    public void extractResources(Collection<Resource> files, File destinationDir) throws IOException {
        // Extract/filter Debian package control files
        // This is done before the files part so that control files can be overridden
        for (String file : CONTROL_RESOURCES) {
            final String path = String.format("/control/%s", file);
            final URL resource = DropwizardMojo.class.getResource(path);
            if (resource != null) {
                final ByteSource source = Resources.asByteSource(resource);
                final File target = new File(destinationDir, path);
                extractResource(source, target, false);
            }
        }

        // Extract/filter passed in files
        for (Resource resource : files) {
            final String path = String.format("/files/%s", resource.getTarget());
            final ByteSource source = resource.getSource();
            final File target = new File(destinationDir, path);
            extractResource(source, target, resource.isFilter());
        }
    }

    private void extractResource(ByteSource source, File target, boolean filter) throws IOException {
        if (filter) {
            filterResource(source, target);
        }
        else {
            copyResource(source, target);
        }
    }
    
    private void copyResource(ByteSource source, File target) throws IOException {
        try (final OutputStream out = createFile(target)) {
            source.copyTo(out);
        }
    }

    private void filterResource(ByteSource source, File target) throws IOException {
        try (final InputStreamReader input = new InputStreamReader(source.openStream())) {
            try (final Writer output = new OutputStreamWriter(createFile(target))) {
                log.debug(String.format("Extracting %s to %s", source, target));
                TEMPLATER.execute(input, output, parameters);
            }
        }
    }

    private OutputStream createFile(File file) throws IOException {
        if (file.getParentFile().mkdirs()) {
            log.debug(String.format("Created resource directory for: %s", file));
        }

        return new FileOutputStream(file);
    }
}
