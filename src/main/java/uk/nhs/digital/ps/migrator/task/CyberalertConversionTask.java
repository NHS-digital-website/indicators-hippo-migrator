package uk.nhs.digital.ps.migrator.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.Cyberalert;
import uk.nhs.digital.ps.migrator.model.hippo.CyberalertFolder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.json.simple.JSONValue;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.migrator.misc.FileHelper.recreate;
import static uk.nhs.digital.ps.migrator.misc.ApiHelper.loadDocumentLookup;
import static uk.nhs.digital.ps.migrator.task.CyberalertConversionTask.Column.*;

public class CyberalertConversionTask implements MigrationTask {

    private static final Logger log = getLogger(CyberalertConversionTask.class);

    private static final String API_URL_ORGANISATIONS = "https://training.nhsd.io/api/documents?_nodetype=website:organisation&_sortOrder=ascending&_offset=";
    private static final String API_URL_PLATFORMS = "https://training.nhsd.io/api/documents?_nodetype=website:platform&_sortOrder=ascending&_offset=";

    private final ExecutionParameters executionParameters;

    private final ImportableFileWriter importableFileWriter;

    private Map<String, String> mapOrganisationToUuid = new HashMap<>();
    private Map<String, String> mapPlatformToUuid = new HashMap<>();

    public CyberalertConversionTask(final ExecutionParameters executionParameters,
                                  final ImportableFileWriter importableFileWriter
    ) {
        this.executionParameters = executionParameters;
        this.importableFileWriter = importableFileWriter;

        loadDocumentLookup(API_URL_ORGANISATIONS, mapOrganisationToUuid, 0);
        loadDocumentLookup(API_URL_PLATFORMS, mapPlatformToUuid, 0);
    }

    @Override
    public boolean isRequested() {
        return executionParameters.getCyberSrcSpreadsheetPath() != null &&
                executionParameters.getCyberalertSrcSpreadsheetName() != null;
    }

    @Override
    public void execute() {

        //mapPlatformToUuid.forEach((k,v) -> System.out.println("key;" +k+ " value:"+v));

        final Path hippoImportDir = executionParameters.getHippoImportDir();
        final Path cyberalertSpreadsheetPath = executionParameters.getCyberSrcSpreadsheetPath();
        final String cyberalertSpreadsheetName = executionParameters.getCyberalertSrcSpreadsheetName();
        final int year = 2015;

        assertRequiredArgs(hippoImportDir, cyberalertSpreadsheetPath, cyberalertSpreadsheetName);

        recreate(hippoImportDir);

        final Workbook workbook = loadWorkbook(cyberalertSpreadsheetPath);

        final Sheet csvSheet = loadSpreadsheet(workbook, cyberalertSpreadsheetName);

        CyberalertConversionTask.Column.init(csvSheet, workbook.getCreationHelper().createFormulaEvaluator());

        CyberalertFolder targetFolder = new CyberalertFolder(null, Integer.toString(year));

        final List<HippoImportableItem> docs = streamRows(csvSheet)
                .skip(1)
                .peek(row -> log.debug("Processing row {}", row.getRowNum() + 1))
                .map(cells -> rowToCyberalertDoc(targetFolder, cells))
                .filter(doc->doc.getDatePublishedCalendar().get(Calendar.YEAR) == year)
                .collect(toList());

//
//        final List<HippoImportableItem> docs = streamRows(csvSheet)
//                .skip(1)
//                .peek(row -> log.debug("Processing row {}", row.getRowNum() + 1))
//                .map(cells -> rowToCyberalertDoc(targetFolder, cells))
//                .collect(toList());

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

    private Cyberalert rowToCyberalertDoc(final CyberalertFolder targetFolder, final Row cells) {

        return new Cyberalert(
                mapPlatformToUuid,
                targetFolder,
                JSONValue.escape(TITLE.getValue(cells)),
                JSONValue.escape(SUMMARY.getValue(cells)),
                JSONValue.escape(SEO_SUMMARY.getValue(cells)),
                JSONValue.escape(SHORT_SUMMARY.getValue(cells)),
                THREAT_ID.getValue(cells),
                THREAT_DEVERITY.getValue(cells),
                THREAT_CATEGORY.getValue(cells),
                THREAT_TYPE.getValue(cells),
                THREAT_VECTOR.getValue(cells),
                DATE_PUBLISHED.getValue(cells),
                DATE_LASTUPDATED.getValue(cells),
                SERVICES.getValue(cells),
                TOPIC.getValue(cells),
                JSONValue.escape(PLATFORM_TEXT.getValue(cells)),
                PLATFORM_AFFECTED.getValue(cells),
                JSONValue.escape(VERSIONS_AFFECTED.getValue(cells)),
                THREAT_HEADER.getValue(cells),
                JSONValue.escape(THREAT_DETAIL.getValue(cells)),
                UPDATE_PUBLISHED1.getValue(cells),
                UPDATE_HEADER1.getValue(cells),
                JSONValue.escape(UPDATE_DETAIL1.getValue(cells)),
                JSONValue.escape(REMEDIATION_INTRODUCTION.getValue(cells)),
                JSONValue.escape(STEP.getValue(cells)),
                JSONValue.escape(REMEDIATION_ACTION.getValue(cells)),
                REMEDIATION_TYPE.getValue(cells),
                JSONValue.escape(INDICATORS_OF_COMPROMISE.getValue(cells)),
                LINK_TO_NCSC.getValue(cells),
                DEFINITIVE_SOURCE_OF_THREAT_UPDATES.getValue(cells),
                CVE_IDENTIFIER.getValue(cells),
                JSONValue.escape(CVE_TEXT.getValue(cells)),
                CVE_STATUS.getValue(cells),
                PUBLICALLY_ACCESSIBLE.getValue(cells)
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
        private static Set<CyberalertConversionTask.Column> allValues = new HashSet<>();

        static final Column TITLE = new Column("Name");
        static final Column SUMMARY = new Column("Summary");
        static final Column SEO_SUMMARY = new Column("Seo Summary");
        static final Column SHORT_SUMMARY = new Column("Short summary");
        static final Column THREAT_ID= new Column("Threat ID");
        static final Column THREAT_DEVERITY = new Column("Threat severity");
        static final Column THREAT_CATEGORY = new Column("Threat category");
        static final Column THREAT_TYPE = new Column("Threat type");
        static final Column THREAT_VECTOR = new Column("Threat vector");
        static final Column DATE_PUBLISHED = new Column("Date published");
        static final Column DATE_LASTUPDATED = new Column("Date last updated");
        static final Column SERVICES = new Column("Services");
        static final Column TOPIC = new Column("Topic");
        static final Column PLATFORM_TEXT = new Column("Platform text");
        static final Column PLATFORM_AFFECTED = new Column("Platform affected");
        static final Column VERSIONS_AFFECTED = new Column("Versions affected");
        static final Column THREAT_HEADER = new Column("Threat header");
        static final Column THREAT_DETAIL = new Column("Threat detail");
        static final Column UPDATE_PUBLISHED1 = new Column("Update published 1");
        static final Column UPDATE_HEADER1 = new Column("Update header 1");
        static final Column UPDATE_DETAIL1 = new Column("Update detail 1");
        static final Column REMEDIATION_INTRODUCTION = new Column("Remediation introduction");
        static final Column STEP = new Column("Step");
        static final Column REMEDIATION_ACTION = new Column("Remediation Action");
        static final Column REMEDIATION_TYPE = new Column("Remediation Type");
        static final Column INDICATORS_OF_COMPROMISE = new Column("Indicators of compromise");
        static final Column LINK_TO_NCSC = new Column("Link to NCSC");
        static final Column DEFINITIVE_SOURCE_OF_THREAT_UPDATES = new Column("Definitive Source of Threat updates");
        static final Column CVE_IDENTIFIER = new Column("CVE identifier");
        static final Column CVE_TEXT = new Column("CVE text");
        static final Column CVE_STATUS = new Column("CVE status");
        static final Column PUBLICALLY_ACCESSIBLE = new Column("Publically accessible");

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
                    .replaceAll("\"", "\\\"")
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
                                    final String cyberalertSpreadsheetName
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

        if (isBlank(cyberalertSpreadsheetName)) {
            throw new IllegalArgumentException(
                    "Required source spreadsheet name was not specified.");
        }
    }
}
