package uk.nhs.digital.ps.migrator.config;

import static java.util.Arrays.asList;
import static uk.nhs.digital.ps.migrator.misc.Descriptor.describe;

import uk.nhs.digital.ps.migrator.misc.Descriptor;

import java.nio.file.Path;
import java.util.List;

public class ExecutionParameters {

    private Path nesstarUnzippedExportDir;
    private Path nesstarZippedExportFile;
    private boolean isNesstarUnzipForce;
    private boolean convertNesstar;
    private Path nesstarAttachmentDownloadDir;
    private Path nesstarCompendiumMappingFile;

    private Path hippoImportDir;
    private Path taxonomyDefinitionImportPath;
    private Path taxonomyDefinitionOutputPath;
    private Path taxonomyMappingImportPath;
    private Path nesstarFieldMappingImportPath;
    private Path migrationReportFilePath;
    private boolean generateImportPackage;
    private Path importPackageDir;

    private Path nationalIndicatorImportPath;  
    private Path nationalIndicatorAttachmentPath;
    private Path gdprSrcSpreadsheetPath;
    private String gdprSrcSpreadsheetName;
    private Path cyberSrcSpreadsheetPath;
    private String organisationSrcSpreadsheetName;
    private String platformSrcSpreadsheetName;
    private String cyberalertSrcSpreadsheetName;

    public Path getNesstarUnzippedExportDir() {
        return nesstarUnzippedExportDir;
    }

    public void setNesstarUnzippedExportDir(final Path nesstarUnzippedExportDir) {
        this.nesstarUnzippedExportDir = nesstarUnzippedExportDir;
    }

    public void setNesstarZippedExportFile(final Path nesstarZippedExportFile) {
        this.nesstarZippedExportFile = nesstarZippedExportFile;
    }

    public Path getNesstarZippedExportFile() {
        return nesstarZippedExportFile;
    }

    public void setNesstarUnzipForce(final boolean isNesstarUnzipForce) {
        this.isNesstarUnzipForce = isNesstarUnzipForce;
    }

    public boolean isNesstarUnzipForce() {
        return isNesstarUnzipForce;
    }

    public boolean isConvertNesstar() {
        return convertNesstar;
    }

    public void setConvertNesstar(final boolean convertNesstar) {
        this.convertNesstar = convertNesstar;
    }

    public Path getHippoImportDir() {
        return hippoImportDir;
    }

    public void setHippoImportDir(final Path hippoImportDir) {
        this.hippoImportDir = hippoImportDir;
    }

    public Path getNesstarAttachmentDownloadDir() {
        return nesstarAttachmentDownloadDir;
    }

    public void setNesstarAttachmentDownloadDir(Path nesstarAttachmentDownloadDir) {
        this.nesstarAttachmentDownloadDir = nesstarAttachmentDownloadDir;
    }

    public Path getNesstarCompendiumMappingFile() {
        return nesstarCompendiumMappingFile;
    }

    public void setNesstarCompendiumMappingFile(final Path nesstarCompendiumMappingFile) {
        this.nesstarCompendiumMappingFile = nesstarCompendiumMappingFile;
    }

    public Path getTaxonomyDefinitionImportPath() {
        return taxonomyDefinitionImportPath;
    }

    public void setTaxonomyDefinitionImportPath(Path taxonomyDefinitionImportPath) {
        this.taxonomyDefinitionImportPath = taxonomyDefinitionImportPath;
    }

    public Path getTaxonomyDefinitionOutputPath() {
        return taxonomyDefinitionOutputPath;
    }

    public void setTaxonomyDefinitionOutputPath(Path taxonomyDefinitionOutputPath) {
        this.taxonomyDefinitionOutputPath = taxonomyDefinitionOutputPath;
    }

    public Path getTaxonomyMappingImportPath() {
        return taxonomyMappingImportPath;
    }

    public void setTaxonomyMappingImportPath(Path taxonomyMappingImportPath) {
        this.taxonomyMappingImportPath = taxonomyMappingImportPath;
    }

    public Path getNesstarFieldMappingImportPath() {
        return nesstarFieldMappingImportPath;
    }

    public void setNesstarFieldMappingImportPath(Path nesstarFieldMappingImportPath) {
        this.nesstarFieldMappingImportPath = nesstarFieldMappingImportPath;
    }

    public Path getMigrationReportFilePath() {
        return migrationReportFilePath;
    }

    public void setMigrationReportFilePath(Path migrationReportFilePath) {
        this.migrationReportFilePath = migrationReportFilePath;
    }

    public Path getNationalIndicatorImportPath() {
        return nationalIndicatorImportPath;
    }

    public void setNationalIndicatorImportPath(Path nationalIndicatorImportPath) {
        this.nationalIndicatorImportPath = nationalIndicatorImportPath;
    }

    public Path getNationalIndicatorAttachmentPath() {
        return nationalIndicatorAttachmentPath;
    }

    public void setNationalIndicatorAttachmentPath(Path nationalIndicatorAttachmentPath) {
        this.nationalIndicatorAttachmentPath = nationalIndicatorAttachmentPath;
    }

    public boolean isGenerateImportPackage() {
        return generateImportPackage;
    }

    public void setGenerateImportPackage(final boolean generateImportPackage) {
        this.generateImportPackage = generateImportPackage;
    }

    public void setImportPackageDir(final Path importPackageDir) {
        this.importPackageDir = importPackageDir;
    }

    public Path getImportPackageDir() {
        return importPackageDir;
    }

    public void setGdprSrcWorkbookPath(final Path gdprSrcSpreadsheetPath) {
        this.gdprSrcSpreadsheetPath = gdprSrcSpreadsheetPath;
    }

    public Path getGdprSrcSpreadsheetPath() {
        return gdprSrcSpreadsheetPath;
    }

    public void setGdprSrcSpreadsheetName(final String gdprSrcSpreadsheetName) {
        this.gdprSrcSpreadsheetName = gdprSrcSpreadsheetName;
    }

    public String getGdprSrcSpreadsheetName() {
        return gdprSrcSpreadsheetName;
    }

    public void setCyberSrcWorkbookPath(final Path cyberSrcSpreadsheetPath) {
        this.cyberSrcSpreadsheetPath = cyberSrcSpreadsheetPath;
    }

    public Path getCyberSrcSpreadsheetPath() {
        return cyberSrcSpreadsheetPath;
    }

    public void setOrganisationSpreadsheetName(final String organisationSrcSpreadsheetName) {
        this.organisationSrcSpreadsheetName = organisationSrcSpreadsheetName;
    }

    public void setPlatformSpreadsheetName(final String platformSrcSpreadsheetName) {
        this.platformSrcSpreadsheetName = platformSrcSpreadsheetName;
    }

    public void setCyberalertSpreadsheetName(final String cyberalertSrcSpreadsheetName) {
        this.cyberalertSrcSpreadsheetName = cyberalertSrcSpreadsheetName;
    }

    public String getOrganisationSrcSpreadsheetName() {
        return organisationSrcSpreadsheetName;
    }

    public String getPlatformSrcSpreadsheetName() {
        return platformSrcSpreadsheetName;
    }

    public String getCyberalertSrcSpreadsheetName() {
        return cyberalertSrcSpreadsheetName;
    }

    public List<Descriptor> descriptions() {
        return asList(
            describe("nesstarUnzippedExportDir", nesstarUnzippedExportDir),
            describe("nesstarZippedExportFile", nesstarZippedExportFile),
            describe("isNesstarUnzipForce", isNesstarUnzipForce),
            describe("convertNesstar", convertNesstar),
            describe("hippoImportDir", hippoImportDir),
            describe("nesstarAttachmentDownloadDir", nesstarAttachmentDownloadDir),
            describe("nesstarCompendiumMappingFile", nesstarCompendiumMappingFile),
            describe("taxonomyDefinitionImportPath", taxonomyDefinitionImportPath),
            describe("taxonomyDefinitionOutputPath", taxonomyDefinitionOutputPath),
            describe("taxonomyMappingImportPath", taxonomyMappingImportPath),
            describe("nesstarFieldMappingImportPath", nesstarFieldMappingImportPath),
            describe("migrationReportFilePath", migrationReportFilePath),
            describe("nationalIndicatorImportPath", nationalIndicatorImportPath),
            describe("nationalIndicatorAttachmentPath", nationalIndicatorAttachmentPath),
            describe("importPackageDir", importPackageDir),
            describe("gdprSrcSpreadsheetPath", gdprSrcSpreadsheetPath),
            describe("gdprSrcSpreadsheetName", gdprSrcSpreadsheetName),
            describe("cyberSrcSpreadsheetPath", cyberSrcSpreadsheetPath),
            describe("organisationSrcSpreadsheetName", organisationSrcSpreadsheetName),
            describe("platformSrcSpreadsheetName", platformSrcSpreadsheetName),
            describe("cyberalertSrcSpreadsheetName", cyberalertSrcSpreadsheetName)
        );
    }
}
