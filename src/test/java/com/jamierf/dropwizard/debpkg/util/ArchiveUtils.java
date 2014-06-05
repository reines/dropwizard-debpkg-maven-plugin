package com.jamierf.dropwizard.debpkg.util;

import com.google.common.io.Files;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public final class ArchiveUtils {

    public static void extractAr(File file, File destination) throws IOException {
        try (final ArArchiveInputStream in = new ArArchiveInputStream(new FileInputStream(file))) {
            extractArchive(in, destination);
        }
    }

    public static void extractTarGzip(File file, File destinaton) throws IOException {
        try (final TarArchiveInputStream in = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(file)))) {
            extractArchive(in, destinaton);
        }
    }

    private static void extractArchive(ArchiveInputStream in, File destination) throws IOException {
        for (ArchiveEntry entry; (entry = in.getNextEntry()) != null; ) {
            final File target = new File(destination, entry.getName());
            if (entry.isDirectory()) {
                target.mkdir();
            }
            else {
                Files.asByteSink(target).writeFrom(in);
            }
        }
    }

    private ArchiveUtils() {}
}
