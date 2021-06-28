package uk.nhs.digital.ps.migrator.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.Organisation;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.OrganisationFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.migrator.misc.FileHelper.recreate;
import static uk.nhs.digital.ps.migrator.task.OrganisationConversionTask.Column.*;

public class OrganisationConversionTask implements MigrationTask {

    private static final Logger log = getLogger(OrganisationConversionTask.class);

    private final ExecutionParameters executionParameters;

    private final ImportableFileWriter importableFileWriter;

    public OrganisationConversionTask(final ExecutionParameters executionParameters,
                                      final ImportableFileWriter importableFileWriter
    ) {
        this.executionParameters = executionParameters;
        this.importableFileWriter = importableFileWriter;
    }

    @Override
    public boolean isRequested() {
        return executionParameters.getCyberSrcSpreadsheetPath() != null &&
                executionParameters.getOrganisationSrcSpreadsheetName() != null;
    }

    @Override
    public void execute() {
        final Path hippoImportDir = executionParameters.getHippoImportDir();
        final Path organisationSpreadsheetPath = executionParameters.getCyberSrcSpreadsheetPath();
        final String organisationSpreadsheetName = executionParameters.getOrganisationSrcSpreadsheetName();

        assertRequiredArgs(hippoImportDir, organisationSpreadsheetPath, organisationSpreadsheetName);

        recreate(hippoImportDir);

        final Workbook workbook = loadWorkbook(organisationSpreadsheetPath);

        final Sheet csvSheet = loadSpreadsheet(workbook, organisationSpreadsheetName);

        Column.init(csvSheet, workbook.getCreationHelper().createFormulaEvaluator());

        OrganisationFolder targetFolder = new OrganisationFolder(null, "Organisation Import");

        final List<HippoImportableItem> docs = streamRows(csvSheet)
            .skip(1)
            .peek(row -> log.debug("Processing row {}", row.getRowNum() + 1))
            .map(cells -> rowToOrganisationDoc(targetFolder, cells))
            .collect(toList());

        docs.add(0, targetFolder);

        reportDuplicatePaths(docs);

        importableFileWriter.writeImportableFiles(docs, hippoImportDir);
    }

    private void reportDuplicatePaths(final List<HippoImportableItem> docs) {

        final Set<String> duplicatePaths = docs
            .stream()
            .map(HippoImportableItem::getJcrPath)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .filter(stringLongEntry -> stringLongEntry.getValue() > 1)
            .map(Entry::getKey)
            .collect(toSet());

        if (!duplicatePaths.isEmpty()) {
            throw new RuntimeException("Duplicate paths detected: " + duplicatePaths);
        }
    }

    private Sheet loadSpreadsheet(final Workbook workbook, final String spreadSheetName) {

        final Sheet spreadsheet = workbook.getSheet(spreadSheetName);
        if (spreadsheet == null) {
            throw new IllegalArgumentException("Sheet '" + spreadSheetName + "' was not found in the workbook.");
        }
        return spreadsheet;
    }

    private Stream<Row> streamRows(Sheet csvSheet) {
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(csvSheet.rowIterator(),Spliterator.ORDERED), false
        );
    }

    private Organisation rowToOrganisationDoc(final OrganisationFolder targetFolder, final Row cells) {

        return new Organisation(
            targetFolder,
            TITLE.getValue(cells),
            SUMMARY.getValue(cells),
                SEO_SUMMARY.getValue(cells),
                SHORT_SUMMARY.getValue(cells),
                PARENTORGANISATION.getValue(cells),
                ABBREVIATION.getValue(cells),
                SYNONYMS.getValue(cells),
                TOPICS.getValue(cells),
                ORGANISATIONTYPE.getValue(cells),
                TELEPHONE.getValue(cells),
                EMAIL.getValue(cells),
                WEBSITE.getValue(cells),
                BUILDINGLOCATION.getValue(cells),
                BUILDINGNAME.getValue(cells),
                STREET.getValue(cells),
                AREA.getValue(cells),
                TOWN.getValue(cells),
                COUNTY.getValue(cells),
                COUNTRY.getValue(cells),
                TYPE.getValue(cells),
                POSTALCODE.getValue(cells),
                LOCATION.getValue(cells),
                CODE.getValue(cells),
                URI.getValue(cells)
        );
    }

    private XSSFWorkbook loadWorkbook(final Path workbookPath) {
        try {
            return new XSSFWorkbook(workbookPath.toFile());
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException("Failed to load workbook " + workbookPath);
        }
    }

    static class Column {
        private static Set<Column> allValues = new HashSet<>();

        static final Column TITLE = new Column("Organisation Name");
        static final Column SUMMARY = new Column("Organisation Summary");
        static final Column SEO_SUMMARY = new Column("Seo Summary");
        static final Column SHORT_SUMMARY = new Column("Short summary");

        static final Column PARENTORGANISATION = new Column("parent organization");
        static final Column ABBREVIATION = new Column("Abbreviation");
        static final Column SYNONYMS = new Column("Synonyms");
        static final Column TOPICS = new Column("Topics");
        static final Column ORGANISATIONTYPE = new Column("Organisation Type");
        static final Column TELEPHONE = new Column("Telephone Number");
        static final Column EMAIL = new Column("Email address");
        static final Column WEBSITE = new Column("Website");
        static final Column BUILDINGLOCATION = new Column("Building location");
        static final Column BUILDINGNAME = new Column("Building name or number");
        static final Column STREET = new Column("Street");
        static final Column AREA = new Column("Area");
        static final Column TOWN = new Column("Town/city");
        static final Column COUNTY = new Column("County");
        static final Column COUNTRY = new Column("Country");
        static final Column TYPE = new Column("Type");
        static final Column POSTALCODE = new Column("Postal code");
        static final Column LOCATION = new Column("Location");
        static final Column CODE = new Column("Code");
        static final Column URI = new Column("URI");

        private static final Map<Column, Integer> columnIndices = new HashMap<>();
        private static FormulaEvaluator formulaEvaluator;

        private String header;

        Column(final String header) {
            this.header = header;

            allValues.add(this);
        }

        public String getHeader() {
            return header;
        }

        String getValue(final Row row) {

            final int columnIndex = columnIndices.get(this);

            log.debug("Processing column {}: '{}'", columnIndex, header);

            final Cell cell = row.getCell(columnIndex);
            if (cell == null) {
                return "";
            }

            log.debug("Processing cell {} ({}) '{}'",  cell.getAddress(), cell.getCellTypeEnum(), header);

            final CellValue cellValue = formulaEvaluator.evaluate(cell);

            String value = cellValue == null
                ? ""
                : CellType.BOOLEAN.equals(cellValue.getCellTypeEnum())
                    ? cellValue.formatAsString()
                    : cellValue.getStringValue();

            if (value == null) {
                value = "";
            }

            value = value
                .trim()
                .replaceAll("\"", "\\\\\"")
                .replaceAll("\r", "")
                .replaceAll("\n", "\\\\n");

            log.debug("Value: {}", value);

            return value.trim();
        }


        static Column findByTitle(final String title) {
            return allValues.stream()
                .filter(column -> column.getHeader().equalsIgnoreCase(title))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unrecognised value: " + title));
        }

        private static void init(final Sheet csvSheet,
                                 final FormulaEvaluator formulaEvaluator
        ) {
            Column.formulaEvaluator = formulaEvaluator;

            final Row headersRow = csvSheet.getRow(csvSheet.getFirstRowNum());
            for (int columnIndex = headersRow.getFirstCellNum(); columnIndex < headersRow.getLastCellNum(); columnIndex++) {
                final Cell cell = headersRow.getCell(columnIndex);
                if (cell == null) {
                    continue;
                }

                final String cellValue = cell.getStringCellValue().trim();

                System.out.println("INDEX: " + columnIndex  + " :" + cellValue);
                if (StringUtils.isNotBlank(cellValue)) {
                    columnIndices.put(Column.findByTitle(cellValue), columnIndex);
                }
            }

            final Set<String> unpopulatedColumns = allValues.stream()
                .filter(header -> !columnIndices.containsKey(header))
                .map(Column::getHeader)
                .collect(toSet());

            if (!unpopulatedColumns.isEmpty()) {
                log.warn("Unpopulated columns found: {}", unpopulatedColumns);
            }
        }
    }

    private void assertRequiredArgs(final Path hippoImportDir,
                                    final Path organisationSrcSpreadsheetPath,
                                    final String organisationSrcSpreadSheetName
    ) {
        if (hippoImportDir == null) {
            throw new IllegalArgumentException(
                "Required Hippo import dir location was not specified.");
        }

        if (organisationSrcSpreadsheetPath == null) {
            throw new IllegalArgumentException(
                "Required source workbook was not specified.");
        }

        if (!Files.isRegularFile(organisationSrcSpreadsheetPath)) {
            throw new IllegalArgumentException(
                "Required source spreadsheet is not a valid file: " + organisationSrcSpreadsheetPath);
        }

        if (isBlank(organisationSrcSpreadSheetName)) {
            throw new IllegalArgumentException(
                "Required source spreadsheet name was not specified.");
        }
    }
}

