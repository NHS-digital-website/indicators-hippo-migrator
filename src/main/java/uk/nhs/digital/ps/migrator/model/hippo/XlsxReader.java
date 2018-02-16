package uk.nhs.digital.ps.migrator.model.hippo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import uk.nhs.digital.ps.migrator.misc.TextHelper;

/**
 * Shared functions for streaming and reading Excel sheets/rows.
 */
public class XlsxReader {
 
    private Map<String, Integer> headerNameIndexPairs;

    /**
     * Initialise the reader. This processes the headers and returns the renaming iterator. Headers are 'cleaned' for improved robustness.
     */
    public Iterator<Row> initAndGetRenamingRowIterator(Path path, final String sheetName){
        Iterator<Row> rowIterator = getRowIterator(path, sheetName);
        Row headerRow = rowIterator.next();
        this.headerNameIndexPairs = streamRow(headerRow)
            .map(XlsxReader::convertHeaderCellToHeaderInfo)
            .collect(Collectors.toMap(Entry<String,Integer>::getKey, item -> item.getValue()));

        return rowIterator;
    }

    /**
     * Returns cell stream from Row.
     */    
    public Stream<Cell> streamRow(Row row) {
        return StreamSupport.stream(((Iterable<Cell>) row::cellIterator).spliterator(), false);
    }

    /**
     * Opens XLSX, loads the requested sheet and returns the row iterator.
     */    
    public Iterator<Row> getRowIterator(Path path, final String sheetName) {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(path.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new RuntimeException("Sheet \'" + sheetName + "\' not found in " + path);
        }


        return sheet.rowIterator();
    }   

    /**
     * Converts a header cell into a simplifed pairing of ColumnName, Index.
     */  
    private static Entry<String,Integer> convertHeaderCellToHeaderInfo(Cell c){
        return new AbstractMap.SimpleEntry<String,Integer>(cleanHeader(c.getStringCellValue()), c.getColumnIndex());
    }

    /**
     * Clean the header name into a desanitised lower case value. This makes it more robust (no spaces etc.).
     */
    public static String cleanHeader(String val) {
        String cleanedColName = val.trim()
            .replaceAll("[^A-z]", "-")
            .toLowerCase();

        return cleanedColName;
    }

    /**
     * Returns index of column name. Uses the same cleaning method before matching.
     */       
    public Integer getColumnIndex(String columnName){
        if (!this.headerNameIndexPairs.containsKey(cleanHeader(columnName))) {
            throw new RuntimeException("There is no column of name " + columnName + ". Please check the input file.");
        }

        return this.headerNameIndexPairs.get(cleanHeader(columnName));    
    }
  
    /**
     * Returns string cell value. Apache code will throw exception if the cell is not of type string.
     */        
    public String getCellValue(String columnName, Row row){
        Cell cell = row.getCell(getColumnIndex(columnName));
        if (cell == null)
            return null;

        String rawValue = cell.getStringCellValue();
        if (rawValue == null)
            return null;


        return TextHelper.escapeSpecialCharsForJson(rawValue);
    }

    /**
     * Returns date cell value. Apache code will throw exception if the cell is not of type date. We generally import into Hippo using strings so this formats using the standard date format.
     */       
    public String getDateValue(String columnName, Row row){
        Cell cell = row.getCell(getColumnIndex(columnName));
        if (cell == null)
            return null;

        Date rawValue = cell.getDateCellValue();
        if (rawValue == null)
            return null;

        return HippoImportableItem.DATE_FORMAT.format(rawValue);
    }        
}