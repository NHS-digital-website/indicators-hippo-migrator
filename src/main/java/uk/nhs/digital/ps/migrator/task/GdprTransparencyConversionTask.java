package uk.nhs.digital.ps.migrator.task;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.migrator.misc.FileHelper.recreate;
import static uk.nhs.digital.ps.migrator.task.GdprTransparencyConversionTask.Column.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.GdprFolder;
import uk.nhs.digital.ps.migrator.model.hippo.GdprTransparencyDocument;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GdprTransparencyConversionTask implements MigrationTask {

    private static final Logger log = getLogger(GdprTransparencyConversionTask.class);

    private final ExecutionParameters executionParameters;

    private final ImportableFileWriter importableFileWriter;

    final Map<Column, String> rightsColumnsToKeys = new HashMap() {{
        put(BEINFORMED,"be-informed");
        put(GETACCESSTOIT,"get-access-to-it");
        put(RECTIFYORCHANGEIT,"rectify-or-change-it");
        put(ERASEORREMOVEIT,"erase-or-remove-it");
        put(RESTRICTORSTOPPROCESSINGIT,"restrict-or-stop-processing-it");
        put(MOVECOPYORTRANSFERIT,"move- copy-or-transfer-it");
        put(OBJECTTOITBEINGPROCESSED,"object-to-it-being-processed-or-used");
        put(KNOWIFDECISIONMADEBYCOMPUTER,"know-if-a-decision-was-made-by-a-computer-rather-than-a-person");
    }};

    public GdprTransparencyConversionTask(final ExecutionParameters executionParameters,
                                          final ImportableFileWriter importableFileWriter
    ) {
        this.executionParameters = executionParameters;
        this.importableFileWriter = importableFileWriter;
    }

    @Override
    public boolean isRequested() {
        return executionParameters.getGdprSrcSpreadsheetPath() != null;
    }

    @Override
    public void execute() {
        final Path hippoImportDir = executionParameters.getHippoImportDir();
        final Path gdprSrcSpreadsheetPath = executionParameters.getGdprSrcSpreadsheetPath();
        final String gdprSrcSpreadsheetName = executionParameters.getGdprSrcSpreadsheetName();

        assertRequiredArgs(hippoImportDir, gdprSrcSpreadsheetPath, gdprSrcSpreadsheetName);

        recreate(hippoImportDir);

        final Workbook workbook = loadWorkbook(gdprSrcSpreadsheetPath);

        final Sheet csvSheet = loadSpreadsheet(workbook, gdprSrcSpreadsheetName);

        Column.init(csvSheet, workbook.getCreationHelper().createFormulaEvaluator());

        GdprFolder targetFolder = new GdprFolder(null, "GDPR Import");

        final List<HippoImportableItem> docs = streamRows(csvSheet)
            .skip(1)
            .peek(row -> log.debug("Processing row {}", row.getRowNum() + 1))
            .map(cells -> rowToGdprDoc(targetFolder, cells))
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

    private GdprTransparencyDocument rowToGdprDoc(final GdprFolder targetFolder, final Row cells) {

        return new GdprTransparencyDocument(
            targetFolder,
            TITLE.getValue(cells),
            DATACONTROLLER.getValue(cells),
            ASSETREFNUMBER.getValue(cells),
            HOWUSEINFORMATION.getValue(cells),
            LawfulBasis.from(LAWFULBASIS.getValue(cells)).getKey(),
            SENSITIVITY.getValue(cells), // Yes/No seem to be correctly translated by JCR to boolean
            OUTSIDEUK.getValue(cells),
            TIMERETAINED.getValue(cells),
            WITHDRAWCONSENT.getValue(cells),
            DATASOURCE.getValue(cells),
            COMPUTERDECISION.getValue(cells),
            LEGALLYWHY.getValue(cells),
            WHOCANACCESS.getValue(cells),
            getRights(cells),
            SUMMARY.getValue(cells),
            SEO_SUMMARY.getValue(cells),
            SHORT_SUMMARY.getValue(cells)
        );
    }

    private String getRights(final Row cells) {

        return Stream.of(
            BEINFORMED,
            GETACCESSTOIT,
            RECTIFYORCHANGEIT,
            ERASEORREMOVEIT,
            RESTRICTORSTOPPROCESSINGIT,
            MOVECOPYORTRANSFERIT,
            OBJECTTOITBEINGPROCESSED,
            KNOWIFDECISIONMADEBYCOMPUTER
        )
            .filter(column -> Boolean.parseBoolean(column.getValue(cells)))
            .map(rightsColumnsToKeys::get)
            .collect(joining("\",\""));
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
        static final Column DATACONTROLLER = new Column("Data Controller");
        static final Column ASSETREFNUMBER = new Column("Information Asset Reference Number");
        static final Column HOWUSEINFORMATION = new Column("How do we use this information");
        static final Column LAWFULBASIS = new Column("Lawful basis");
        static final Column SENSITIVITY = new Column("Sensitive");
        static final Column OUTSIDEUK = new Column("Transferred outside UK");
        static final Column TIMERETAINED = new Column("Time retained");

        static final Column WITHDRAWCONSENT = new Column("Withdraw consent");
        static final Column DATASOURCE = new Column("Data source");
        static final Column COMPUTERDECISION = new Column("Computer decision");
        static final Column LEGALLYWHY = new Column("Legally why");
        static final Column WHOCANACCESS = new Column("Who can access");

        static final Column SUMMARY = new Column("Summary");
        static final Column SEO_SUMMARY = new Column("Seo Summary");
        static final Column SHORT_SUMMARY = new Column("Short summary");

        //  RIGHTS:
        static final Column BEINFORMED = new Column("Be informed,");
        static final Column GETACCESSTOIT = new Column("Get access to it,");
        static final Column RECTIFYORCHANGEIT = new Column("Rectify or change it,");
        static final Column ERASEORREMOVEIT = new Column("Erase or remove it,");
        static final Column RESTRICTORSTOPPROCESSINGIT = new Column("Restrict or stop processing it,");
        static final Column MOVECOPYORTRANSFERIT = new Column("Move, copy or transfer it,");
        static final Column OBJECTTOITBEINGPROCESSED = new Column("Object to it being processed or used,");
        static final Column KNOWIFDECISIONMADEBYCOMPUTER = new Column("Know if a decision was made by a computer rather than a person");


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
                                    final Path gdprSrcSpreadsheetPath,
                                    final String gdprSrcSpreadSheetName
    ) {
        if (hippoImportDir == null) {
            throw new IllegalArgumentException(
                "Required Hippo import dir location was not specified.");
        }

        if (gdprSrcSpreadsheetPath == null) {
            throw new IllegalArgumentException(
                "Required source workbook was not specified.");
        }

        if (!Files.isRegularFile(gdprSrcSpreadsheetPath)) {
            throw new IllegalArgumentException(
                "Required source spreadsheet is not a valid file: " + gdprSrcSpreadsheetPath);
        }

        if (isBlank(gdprSrcSpreadSheetName)) {
            throw new IllegalArgumentException(
                "Required source spreadsheet name was not specified.");
        }
    }

    public enum LawfulBasis {
        CONSENT("consent", "Consent"),
        CONTRACT("contract", "Contract"),
        LEGAL_OBLIGATION("legal-obligation", "Legal obligation"),
        VITAL_INTERESTS("vital-interests", "Vital interests"),
        PUBLIC_TASK("public-task", "Public task"),
        LEGITIMATE_INTEREST("legitimate-interests", "Legitimate interests"),
        // Few odd values present in the spreadsheet
        EMPTY("", ""),
        ZERO("", "0.0");

        private final String key;
        private final String displayValue;

        LawfulBasis(final String key, final String displayValue) {
            this.key = key;
            this.displayValue = displayValue;
        }

        public String getKey() {
            return key;
        }

        public String getSpreadsheetValue() {
            return displayValue;
        }

        public static LawfulBasis from(final String value) {
            return Arrays.stream(LawfulBasis.values())
                .filter(candidate -> candidate.getSpreadsheetValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unrecognised value: " + value));
        }
    }
}

