package uk.nhs.digital.ps.migrator.misc;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FileHelper {

    /**
     * Deletes given directory if it doesn't exist and recreates it (together with
     * its ancestor directories, if missing).
     */
    public static void recreate(final Path... directories) {

        Arrays.stream(directories).forEach(directory -> {
            try {
                FileUtils.deleteDirectory(directory.toFile());
                Files.createDirectories(directory);

            } catch (final IOException e) {
                throw new UncheckedIOException("Failed to recreate folders", e);
            }
        });
    }

    /**
     * Copies entire content of the source directory to the target one.
     */
    public static void copyContent(final Path sourceDirectory, final Path targetDirectory) {

        try {
            FileUtils.copyDirectory(sourceDirectory.toFile(), targetDirectory.toFile());
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to copy content of " + sourceDirectory + " to " + targetDirectory, e);
        }
    }

    public static void deleteFile(final Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Failed to delete " + path, exception);
        }
    }
}
