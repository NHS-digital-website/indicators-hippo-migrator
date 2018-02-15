package uk.nhs.digital.ps.migrator.task;

import org.slf4j.Logger;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.migrator.misc.FileHelper.*;

public class GenerateImportPackageTask implements MigrationTask {

    private final static Logger log = getLogger(GenerateImportPackageTask.class);

    private final ExecutionParameters executionParameters;

    public GenerateImportPackageTask(final ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }

    @Override
    public boolean isRequested() {
        return executionParameters.isGenerateImportPackage();
    }

    @Override
    public void execute() {
        // Import package structure:
        // - exim/          - EXIM JSON import files
        // - attachments/   - attachment files

        final Path importPackageDir = executionParameters.getImportPackageDir();
        recreate(importPackageDir);

        final Path importPackageSrcDir = Paths.get(importPackageDir.toString(), "src");
        final Path eximDir = Paths.get(importPackageSrcDir.toString(), "exim");
        final Path attachmentsDir = Paths.get(importPackageSrcDir.toString(), "attachments");

        recreate(
            importPackageSrcDir,
            eximDir,
            attachmentsDir
        );

        log.info("Copying content into the archive source dir {}", importPackageSrcDir);
        copyContent(executionParameters.getHippoImportDir(), eximDir);
        copyContent(executionParameters.getNesstarAttachmentDownloadDir(), attachmentsDir);
        log.info("Done.");

        generateImportPackageZipFile("import-package.zip", importPackageDir, importPackageSrcDir);
    }

    public void generateImportPackageZipFile(final String archiveFileName,
                                             final Path targetDir,
                                             final Path sourceDir
    ) {
        final Path importPackageZipFilePath = Paths.get(
            targetDir.toString(),
            archiveFileName
        );

        try {

            deleteFile(importPackageZipFilePath);

            final List<Path> pathsToArchive = findFilePathsToArchiveWithin(sourceDir);

            log.info("Generating import package archive {}", importPackageZipFilePath);
            try (final ZipOutputStream zipOutputStream =
                     new ZipOutputStream(new FileOutputStream(importPackageZipFilePath.toFile()))) {

                long zippedFilesCount = 0;

                for (final Path path: pathsToArchive) {
                    final ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                    zipOutputStream.putNextEntry(zipEntry);

                    final byte[] buffer = new byte[1024];
                    try (final FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
                        for (int len = fileInputStream.read(buffer); len > 0; len = fileInputStream.read(buffer)) {
                            zipOutputStream.write(buffer, 0, len);
                        }
                    }

                    zipOutputStream.closeEntry();
                    zippedFilesCount++;

                    if (zippedFilesCount == pathsToArchive.size() || zippedFilesCount % 100 == 0) {
                        log.info("Files zipped: {}/{}", zippedFilesCount, pathsToArchive.size());
                    }
                }
            }

            log.info("Archive file size: {}", byteCountToDisplaySize(Files.size(importPackageZipFilePath)));

        } catch (final Exception exception) {
            throw new RuntimeException("Failed to generate import package " + importPackageZipFilePath, exception);

        }
    }

    private List<Path> findFilePathsToArchiveWithin(final Path sourceDir) {
        try {
            return Files.walk(sourceDir)
                .filter(path -> Files.isRegularFile(path))
                .collect(toList());
        } catch (final IOException exception) {
            throw new UncheckedIOException("Failed to find paths to archive in " + sourceDir, exception);
        }
    }
}
