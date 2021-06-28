package uk.nhs.digital.ps.migrator.config;

import static java.util.Arrays.asList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import uk.nhs.digital.ps.migrator.misc.MigratorExitCodeGenerator;
import uk.nhs.digital.ps.migrator.model.hippo.MappedFieldsImporter;
import uk.nhs.digital.ps.migrator.model.hippo.NationalIndicatorMigrator;
import uk.nhs.digital.ps.migrator.model.hippo.TaxonomyMigrator;
import uk.nhs.digital.ps.migrator.model.hippo.XlsxReader;
import uk.nhs.digital.ps.migrator.report.MigrationReport;
import uk.nhs.digital.ps.migrator.task.*;
import uk.nhs.digital.ps.migrator.task.importables.CcgImportables;
import uk.nhs.digital.ps.migrator.task.importables.CompendiumImportables;
import uk.nhs.digital.ps.migrator.task.importables.NhsOutcomesFrameworkImportables;
import uk.nhs.digital.ps.migrator.task.importables.SocialCareImportables;

import java.util.List;

@Configuration
public class MigratorConfiguration {

    @Bean
    public List<MigrationTask> tasks(final ExecutionParameters executionParameters,
                                     final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory,
                                     final SocialCareImportables socialCareImportables,
                                     final CcgImportables ccgImportables,
                                     final NhsOutcomesFrameworkImportables nhsOutcomesFrameworkImportables,
                                     final CompendiumImportables compendiumImportables,
                                     final ImportableFileWriter importableFileWriter,
                                     final MigrationReport migrationReport,
                                     final TaxonomyMigrator taxonomyMigrator,
                                     final NationalIndicatorMigrator nationalIndicatorMigrator) {

        return asList(
                new UnzipNesstarExportFileTask(executionParameters),
                new GenerateNesstarImportContentTask(
                        executionParameters,
                        clinicalIndicatorsImportableItemsFactory,
                        socialCareImportables,
                        ccgImportables,
                        nhsOutcomesFrameworkImportables,
                        compendiumImportables,
                        importableFileWriter,
                        migrationReport,
                        taxonomyMigrator
                ),
                new GenerateTaxonomyTask(
                        executionParameters,
                        taxonomyMigrator
                ),
                new GenerateIndicatorImportContentTask(
                        executionParameters,
                        nationalIndicatorMigrator,
                        importableFileWriter
                ),
                new GenerateImportPackageTask(executionParameters),
                new GdprTransparencyConversionTask(executionParameters,
                    importableFileWriter
                ),
                new OrganisationConversionTask(executionParameters,
                        importableFileWriter
                ),
                new PlatformConversionTask(executionParameters,
                        importableFileWriter
                ),
                new CyberalertConversionTask(executionParameters,
                        importableFileWriter
                )
        );
    }

    @Bean
    public ExecutionParameters sharedTaskParameters() {
        return new ExecutionParameters();
    }

    @Bean
    public MigrationReport migrationReport(final ExecutionParameters executionParameters,
                                           final MigratorExitCodeGenerator migratorExitCodeGenerator
    ) {
        return new MigrationReport(executionParameters, migratorExitCodeGenerator);
    }

    @Bean
    public ExecutionConfigurator commandLineArgsParser(final ExecutionParameters executionParameters) {
        return new ExecutionConfigurator(executionParameters);
    }

    @Bean
    public ClinicalIndicatorsImportableItemsFactory importableItemsFactory(final ExecutionParameters executionParameters,
                                                                           final MigrationReport migrationReport,
                                                                           final TaxonomyMigrator taxonomyMigrator,
                                                                           final MappedFieldsImporter mappedFieldsImporter)
    {
        return new ClinicalIndicatorsImportableItemsFactory(executionParameters, migrationReport, taxonomyMigrator, mappedFieldsImporter);
    }

    @Bean
    public MigratorExitCodeGenerator migratorExitCodeGenerator() {
        return new MigratorExitCodeGenerator();
    }

    @Bean
    public ImportableFileWriter importableFileWriter(final MigrationReport migrationReport) {
        return new ImportableFileWriter(migrationReport);
    }

    @Bean
    public CcgImportables ccgImportables(final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory) {
        return new CcgImportables(clinicalIndicatorsImportableItemsFactory);
    }

    @Bean
    public SocialCareImportables socialCareImportables(final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory) {
        return new SocialCareImportables(clinicalIndicatorsImportableItemsFactory);
    }

    @Bean
    public NhsOutcomesFrameworkImportables nhsOutcomesFrameworkImportables(final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory) {
        return new NhsOutcomesFrameworkImportables(clinicalIndicatorsImportableItemsFactory);
    }

    @Bean
    public CompendiumImportables compendiumImportables(final ExecutionParameters executionParameters,
                                                       final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory,
                                                       final MigrationReport migrationReport
    ) {
        return new CompendiumImportables(executionParameters, clinicalIndicatorsImportableItemsFactory, migrationReport);
    }

    @Bean
    public TaxonomyMigrator taxonomyMigrator(final MigrationReport migrationReport,
                                             final ExecutionParameters executionParameters,
                                             final XlsxReader xslReader) {
        return new TaxonomyMigrator(migrationReport, executionParameters, xslReader);
    }

    @Bean
    public MappedFieldsImporter mappedFieldsImporter(final MigrationReport migrationReport,
                                                     final ExecutionParameters executionParameters) {
        return new MappedFieldsImporter(migrationReport, executionParameters);
    }

    @Bean
    public NationalIndicatorMigrator indicatorMigrator(final ExecutionParameters executionParameters, final XlsxReader xslReader) {
        return new NationalIndicatorMigrator(executionParameters, xslReader);
    }        

    @Bean
    @Scope("prototype")
    public XlsxReader xslReader() {
        return new XlsxReader();
    }        
}
