package uk.nhs.digital.ps.migrator.misc;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {

    public static void recreate(final Path dir) {
        try {
            FileUtils.deleteDirectory(dir.toFile());
            Files.createDirectories(dir);

        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
