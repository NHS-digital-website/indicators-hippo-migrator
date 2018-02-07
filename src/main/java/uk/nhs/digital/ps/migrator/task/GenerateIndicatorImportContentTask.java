package uk.nhs.digital.ps.migrator.task;

import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.NationalIndicatorHippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.NationalIndicatorMigrator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static uk.nhs.digital.ps.migrator.misc.FileHelper.recreate;

public class GenerateIndicatorImportContentTask implements MigrationTask {

    private final ExecutionParameters executionParameters;
    private final NationalIndicatorMigrator nationalIndicatorMigrator;
    private final ImportableFileWriter importableFileWriter;

    public GenerateIndicatorImportContentTask(final ExecutionParameters executionParameters, NationalIndicatorMigrator nationalIndicatorMigrator,final ImportableFileWriter importableFileWriter) {
        this.executionParameters = executionParameters;
        this.nationalIndicatorMigrator = nationalIndicatorMigrator;
        this.importableFileWriter = importableFileWriter;
    }

    @Override
    public boolean isRequested() {
        return executionParameters.getNationalIndicatorImportPath() != null;
    }

    @Override
    public void execute() {

        Path indicatorsImportPath = executionParameters.getNationalIndicatorImportPath();
        Path hippoImportDir = executionParameters.getHippoImportDir();
        assertRequiredArgs(indicatorsImportPath, hippoImportDir);

        List<NationalIndicatorHippoImportableItem> indicators = nationalIndicatorMigrator.readIndicators();

        recreate(hippoImportDir);

        importableFileWriter.writeImportableFiles(indicators, hippoImportDir);
    }

    private void assertRequiredArgs(final Path importPath,
                                    final Path outputPath) {

        if (outputPath == null) {
            throw new IllegalArgumentException("Required Hippo Import Path was not specified.");
        }

        if (importPath == null) {
            throw new IllegalArgumentException("Required Indicator Import Path was not specified.");
        }

        if (!Files.isRegularFile(importPath)) {
            throw new IllegalArgumentException(
                "Indicator Import Path does not exist: " + importPath
            );
        }
    }
}
