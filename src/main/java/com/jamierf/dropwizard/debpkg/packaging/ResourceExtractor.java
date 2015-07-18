package com.jamierf.dropwizard.debpkg.packaging;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.jamierf.dropwizard.debpkg.DropwizardMojo;
import com.jamierf.dropwizard.debpkg.resource.Resource;
import com.jamierf.dropwizard.debpkg.template.Templater;
import org.vafer.jdeb.Console;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class ResourceExtractor {

    private static final Templater TEMPLATER = Templater.getDefault();
    private static final Collection<String> CONTROL_RESOURCES = ImmutableSet.of(
            "control", "conffiles", "preinst", "postinst", "prerm", "postrm"
    );

    private final Map<String, Object> parameters;
    private final Console log;

    public ResourceExtractor(final Map<String, Object> parameters, final Console log) {
        this.parameters = parameters;
        this.log = log;
    }

    public void extractResources(final Collection<Resource> files, final File destinationDir) throws IOException {
        // Extract/filter Debian package control files
        // This is done before the files part so that control files can be overridden
        for (String file : CONTROL_RESOURCES) {
            final String path = String.format("/control/%s", file);
            final URL resource = DropwizardMojo.class.getResource(path);
            if (resource != null) {
                final ByteSource source = Resources.asByteSource(resource);
                final File target = new File(destinationDir, path);
                extractResource(source, target, true);
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

    private void extractResource(final ByteSource source, final File target, final boolean filter) throws IOException {
        log.debug(String.format("Extracting %s to %s", source, target));
        if (filter) {
            filterResource(source, target);
        }
        else {
            copyResource(source, target);
        }
    }

    private void copyResource(final ByteSource source, final File target) throws IOException {
        try (final OutputStream out = createFile(target)) {
            source.copyTo(out);
        }
    }

    private void filterResource(final ByteSource source, final File target) throws IOException {
        try (final InputStreamReader input = new InputStreamReader(source.openStream())) {
            try (final Writer output = new OutputStreamWriter(createFile(target))) {
                TEMPLATER.execute(input, output, target.getAbsolutePath(), parameters);
            }
        }
    }

    private OutputStream createFile(final File file) throws IOException {
        if (file.getParentFile().mkdirs()) {
            log.debug(String.format("Created resource directory for: %s", file));
        }

        return new FileOutputStream(file);
    }
}
