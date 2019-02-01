package uk.nhs.digital.ps.migrator.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.misc.TextHelper;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.Platform;
import uk.nhs.digital.ps.migrator.model.hippo.PlatformFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.migrator.misc.ApiHelper.loadDocumentLookup;
import static uk.nhs.digital.ps.migrator.misc.FileHelper.recreate;
import static uk.nhs.digital.ps.migrator.task.PlatformConversionTask.Column.*;

public class PlatformConversionTask implements MigrationTask {

    private static final Logger log = getLogger(PlatformConversionTask.class);

    private static final String URL_API_ORGANISATIONS = "https://training.nhsd.io/api/documents?_nodetype=website:platform&_sortOrder=ascending&_offset=";

    private final ExecutionParameters executionParameters;

    private final ImportableFileWriter importableFileWriter;

    private Map<String, String> mapOrganisationToUuid = new HashMap<>();


    public PlatformConversionTask(final ExecutionParameters executionParameters,
                                  final ImportableFileWriter importableFileWriter
    ) {
        this.executionParameters = executionParameters;
        this.importableFileWriter = importableFileWriter;

        loadDocumentLookup(URL_API_ORGANISATIONS, mapOrganisationToUuid, 0);
    }

    @Override
    public boolean isRequested() {
        return executionParameters.getCyberSrcSpreadsheetPath() != null &&
                executionParameters.getPlatformSrcSpreadsheetName() != null;
    }

    @Override
    public void execute() {

        //mapOrganisationToUuid.forEach((k,v) -> System.out.println("key;" +k+ " value:"+v));

        final Path hippoImportDir = executionParameters.getHippoImportDir();
        final Path platformSpreadsheetPath = executionParameters.getCyberSrcSpreadsheetPath();
        final String platformSpreadsheetName = executionParameters.getPlatformSrcSpreadsheetName();

        assertRequiredArgs(hippoImportDir, platformSpreadsheetPath, platformSpreadsheetName);

        recreate(hippoImportDir);

        final Workbook workbook = loadWorkbook(platformSpreadsheetPath);

        final Sheet csvSheet = loadSpreadsheet(workbook, platformSpreadsheetName);

        Column.init(csvSheet, workbook.getCreationHelper().createFormulaEvaluator());

        PlatformFolder targetFolder = new PlatformFolder(null, "Platform Import");

        final List<HippoImportableItem> docs = streamRows(csvSheet)
            .skip(1)
            .peek(row -> log.debug("Processing row {}", row.getRowNum() + 1))
            .map(cells -> rowToPlatformDoc(targetFolder, cells))
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

    private Platform rowToPlatformDoc(final PlatformFolder targetFolder, final Row cells) {

        //String supplierName = SUPPLIER.getValue(cells).trim().toLowerCase().replace(" ", "-").replace(".", "");
        String supplierName = TextHelper.normaliseToJcrPathName(SUPPLIER.getValue(cells).trim());
        String supplierUuid = "";

        if (!supplierName.isEmpty()) {
            if (mapOrganisationToUuid.containsKey(supplierName)) {
                supplierUuid = mapOrganisationToUuid.get(supplierName);
            } else {
                throw new RuntimeException("Organisation (Supplier) mapping not found: " + supplierName);
            }
        }

        String resellerName = TextHelper.normaliseToJcrPathName(RESELLER.getValue(cells).trim());
        String resellerUuid = "";

        if (!resellerName.isEmpty()) {
            if (mapOrganisationToUuid.containsKey(resellerName)) {
                resellerUuid = mapOrganisationToUuid.get(resellerName);
            } else {
                throw new RuntimeException("Organisation (Reseller) mapping not found: " + resellerName);
            }
        }

        return new Platform(
            targetFolder,
            TITLE.getValue(cells),
            SUMMARY.getValue(cells),
            SEO_SUMMARY.getValue(cells),
            SHORT_SUMMARY.getValue(cells),
            PLATFORMTYPE.getValue(cells),
            ABBREVIATION.getValue(cells),
            VERSIONNUMBER.getValue(cells),
            VERSIONSTATUS.getValue(cells),
            VERSIONURL.getValue(cells),
                supplierName,
                supplierUuid,
                resellerName,
                resellerUuid,
            PLATFORMURL.getValue(cells),
            TOPICS.getValue(cells)
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

        static final Column TITLE = new Column("Title");
        static final Column SUMMARY = new Column("Summary");
        static final Column SEO_SUMMARY = new Column("Seo Summary");
        static final Column SHORT_SUMMARY = new Column("Short summary");
        static final Column PLATFORMTYPE = new Column("Platform Type");
        static final Column ABBREVIATION = new Column("Abbreviation");
        static final Column VERSIONNUMBER = new Column("Version Number");
        static final Column VERSIONSTATUS = new Column("Version Status");
        static final Column VERSIONURL = new Column("Version URL");
        static final Column SUPPLIER = new Column("Supplier (link to doc)");
        static final Column RESELLER = new Column("Reseller");
        static final Column PLATFORMURL = new Column("Platform URL");
        static final Column TOPICS = new Column("Topics");

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
                                    final Path cyberSrcSpreadsheetPath,
                                    final String platformSrcSpreadSheetName
    ) {
        if (hippoImportDir == null) {
            throw new IllegalArgumentException(
                "Required Hippo import dir location was not specified.");
        }

        if (cyberSrcSpreadsheetPath == null) {
            throw new IllegalArgumentException(
                "Required source workbook was not specified.");
        }

        if (!Files.isRegularFile(cyberSrcSpreadsheetPath)) {
            throw new IllegalArgumentException(
                "Required source spreadsheet is not a valid file: " + cyberSrcSpreadsheetPath);
        }

        if (isBlank(platformSrcSpreadSheetName)) {
            throw new IllegalArgumentException(
                "Required source spreadsheet name was not specified.");
        }
    }
}

