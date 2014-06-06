package com.jamierf.dropwizard.debpkg.packaging;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.jamierf.dropwizard.debpkg.config.PgpConfiguration;
import com.jamierf.dropwizard.debpkg.resource.Resource;
import com.jamierf.dropwizard.debpkg.resource.StringResource;
import com.jamierf.dropwizard.debpkg.template.MissingParameterException;
import com.jamierf.dropwizard.debpkg.util.ArchiveUtils;
import com.jamierf.dropwizard.debpkg.util.SystemConsole;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.tools.tar.TarEntry;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.vafer.jdeb.Console;
import org.vafer.jdeb.PackagingException;
import org.vafer.jdeb.shaded.bc.openpgp.PGPException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Collection;
import java.util.Collections;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PackageBuilderTest {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final Console LOG = new SystemConsole();
    private static final String NAME = "test";
    private static final String DESCRIPTION = "functional test";
    private static final String USER = "timmy";
    private static final String PGP_ALIAS = "FFC5302F";
    private static final String PGP_PASSWORD = "test";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File inputDir;
    private ResourceExtractor extractor;
    private PackageBuilder builder;

    @Before
    public void setUp() throws IOException {
        inputDir = temporaryFolder.newFolder("package");
        assertTrue(new File(inputDir, "control").mkdir());

        extractor = new ResourceExtractor(ImmutableMap.<String, Object>builder()
                .put("deb", ImmutableMap.of("name", NAME, "version", "1.0-SNAPSHOT", "maintainer", "Jamie Furness"))
                .put("project", ImmutableMap.of("description", DESCRIPTION, "artifactId", "com.jamierf.dropwizard"))
                .put("jvm", ImmutableMap.of("packageName", "openjdk-7-jdk", "packageVersion", ""))
                .put("unix", ImmutableMap.of("user", USER))
                .put("path", ImmutableMap.of("logDirectory", "/tmp/logs"))
                .build(), LOG);

        final File keyring = temporaryFolder.newFile();
        Resources.asByteSource(PackageBuilderTest.class.getResource("private.asc")).copyTo(Files.asByteSink(keyring));

        builder = new PackageBuilder(NAME, DESCRIPTION, URI.create("http://www.example.org"), LOG, Optional.of(
                new PgpConfiguration().setAlias(PGP_ALIAS).setKeyring(keyring).setPassphrase(PGP_PASSWORD)
        ));
    }

    private File createPackage(Collection<Resource> resources) throws IOException, PackagingException {
        extractor.extractResources(resources, inputDir);

        final File debFile = temporaryFolder.newFile("package.deb");
        builder.createPackage(resources, inputDir, debFile);
        return debFile;
    }

    @Test
    public void testCreatesPackageWithoutPgp() throws IOException, PackagingException {
        builder = new PackageBuilder(NAME, DESCRIPTION, URI.create("http://www.example.org"), LOG, Optional.<PgpConfiguration>absent());
        testCreatesPackage();
    }

    @Test(expected = MissingParameterException.class)
    public void testFailsOnMissingTemplateVariables() throws IOException, PackagingException {
        createPackage(ImmutableList.<Resource>of(
                new StringResource("hello {{{missing.variable}}}", true, "/tmp/test.txt", USER, USER, TarEntry.DEFAULT_FILE_MODE)
        ));
    }

    @Test(expected = PackagingException.class)
    public void testPackageRequiresContents() throws IOException, PackagingException {
        createPackage(Collections.<Resource>emptyList());
    }

    @Test
    public void testCreatesPackage() throws IOException, PackagingException {
        final File debFile = createPackage(ImmutableList.<Resource>of(
                new StringResource("hello world", true, "/tmp/test.txt", USER, USER, TarEntry.DEFAULT_FILE_MODE)
        ));
        assertTrue(debFile.exists());
    }

    @Test
    public void testPackageSignature() throws IOException, PackagingException, PGPException, SignatureException, org.bouncycastle.openpgp.PGPException, NoSuchProviderException {
        final File debFile = createPackage(ImmutableList.<Resource>of(
                new StringResource("hello world", true, "/tmp/test.txt", USER, USER, TarEntry.DEFAULT_FILE_MODE)
        ));

        final File packageDir = temporaryFolder.newFolder();
        ArchiveUtils.extractAr(debFile, packageDir);

        final File pgpSignatureFile = new File(packageDir, "_gpgorigin");
        assertTrue(pgpSignatureFile.exists());

        try (final InputStream keyringIn = PGPUtil.getDecoderStream(PackageBuilderTest.class.getResourceAsStream("public.asc"))) {
            try (final InputStream signatureIn = PGPUtil.getDecoderStream(new FileInputStream(pgpSignatureFile))) {
                final PGPPublicKey publicKey = ((PGPPublicKeyRing) new PGPPublicKeyRingCollection(keyringIn).getKeyRings().next()).getPublicKey();
                final PGPSignature signature = ((PGPSignatureList) new PGPObjectFactory(signatureIn).nextObject()).get(0);
                signature.initVerify(publicKey, BouncyCastleProvider.PROVIDER_NAME);

                signature.update(Files.asByteSource(new File(packageDir, "debian-binary")).read());
                signature.update(Files.asByteSource(new File(packageDir, "control.tar.gz")).read());
                signature.update(Files.asByteSource(new File(packageDir, "data.tar.gz")).read());

                assertTrue(signature.verify());
            }
        }
    }

    @Test
    public void testValidArchive() throws IOException, PackagingException {
        final File debFile = createPackage(ImmutableList.<Resource>of(
                new StringResource("hello world", true, "/tmp/test.txt", USER, USER, TarEntry.DEFAULT_FILE_MODE)
        ));

        final File packageDir = temporaryFolder.newFolder();
        ArchiveUtils.extractAr(debFile, packageDir);
    }

    @Test
    public void testExpectedControlFiles() throws IOException, PackagingException {
        final File debFile = createPackage(ImmutableList.<Resource>of(
                new StringResource("hello world", true, "/tmp/test.txt", USER, USER, TarEntry.DEFAULT_FILE_MODE)
        ));

        final File packageDir = temporaryFolder.newFolder();
        ArchiveUtils.extractAr(debFile, packageDir);

        final File controlDir = temporaryFolder.newFolder();
        ArchiveUtils.extractTarGzip(new File(packageDir, "control.tar.gz"), controlDir);

        assertTrue(new File(controlDir, "control").exists());
    }

    @Test
    public void testExpectedDataFiles() throws IOException, PackagingException {
        final File debFile = createPackage(ImmutableList.<Resource>of(
                new StringResource("hello world", true, "/tmp/test.txt", USER, USER, TarEntry.DEFAULT_FILE_MODE)
        ));

        final File packageDir = temporaryFolder.newFolder();
        ArchiveUtils.extractAr(debFile, packageDir);

        final File dataDir = temporaryFolder.newFolder();
        ArchiveUtils.extractTarGzip(new File(packageDir, "data.tar.gz"), dataDir);

        final File testFile = new File(dataDir, "/tmp/test.txt");
        assertTrue(testFile.exists());
    }

    @Test
    public void testTemplatesCorrectlyExecuted() throws IOException, PackagingException {
        final File debFile = createPackage(ImmutableList.<Resource>of(
                new StringResource("hello {{{deb.name}}}", true, "/tmp/test.txt", USER, USER, TarEntry.DEFAULT_FILE_MODE)
        ));

        final File packageDir = temporaryFolder.newFolder();
        ArchiveUtils.extractAr(debFile, packageDir);

        final File dataDir = temporaryFolder.newFolder();
        ArchiveUtils.extractTarGzip(new File(packageDir, "data.tar.gz"), dataDir);

        final File testFile = new File(dataDir, "/tmp/test.txt");
        assertTrue(testFile.exists());
        assertEquals("hello test", Files.asCharSource(testFile, StandardCharsets.UTF_8).read().trim());
    }

    @Test
    public void testFileOwnershipAndPermissions() throws IOException, PackagingException {
        final File debFile = createPackage(ImmutableList.<Resource>of(
                new StringResource("hello world", true, "/test.txt", USER, USER, 0764)
        ));

        final File packageDir = temporaryFolder.newFolder();
        ArchiveUtils.extractAr(debFile, packageDir);

        try (final TarArchiveInputStream in = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(new File(packageDir, "data.tar.gz"))))) {
            final TarArchiveEntry entry = in.getNextTarEntry();
            assertEquals("./test.txt", entry.getName());
            assertEquals(USER, entry.getUserName());
            assertEquals(USER, entry.getGroupName());
            assertEquals(0764, entry.getMode());
        }
    }
}
