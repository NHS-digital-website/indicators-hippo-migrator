package uk.nhs.digital.ps.migrator.model.hippo;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import uk.nhs.digital.ps.migrator.config.ExecutionParameters;

/**
 * This class is responsible for loading national indicators to import into hippo.
 */
public class NationalIndicatorMigrator extends BaseXlsReader {

    private ExecutionParameters executionParameters;
    private static final String INDICATORS_SHEET_NAME = "Indicators";
    private static final String TITLE_COLUMN = "Title";

    public NationalIndicatorMigrator(ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }

    public List<NationalIndicatorHippoImportableItem> readIndicators(){
        Path indicatorImportPath = executionParameters.getNationalIndicatorImportPath();
        if (indicatorImportPath == null) {
            throw new RuntimeException("There is no indicator import path.");
        }

        List<NationalIndicatorHippoImportableItem> indicators = new ArrayList<NationalIndicatorHippoImportableItem>();

        Iterator<Row> rowIterator = getRowIterator(indicatorImportPath, INDICATORS_SHEET_NAME);

        Row headerRow = rowIterator.next();

        List<Integer> titleCols = streamRow(headerRow)
        .filter(cell -> cell.getStringCellValue().equals(TITLE_COLUMN))
        .map(Cell::getColumnIndex)
        .collect(toList());

        if (titleCols.size() != 1) {
            throw new RuntimeException("Spreadsheet needs to have a single Title column.");
        }

        int titleCol = titleCols.get(0);

        //Build up collection of National Indicators. We do not specify a root folder as it is expected that one already exists ("national-indicator-library"), see NationalIndicatorHippoImportableItem.getRootPathPrefix()

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String title = row.getCell(titleCol).getStringCellValue().trim();
            indicators.add(new NationalIndicator(null, title, title));
        }

        return indicators;
    }
}