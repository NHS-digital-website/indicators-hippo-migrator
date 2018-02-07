package uk.nhs.digital.ps.migrator.model.hippo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Shared functions for streaming and reading Excel sheets/rows.
 */
public class BaseXlsReader {
 
    protected static Stream<Cell> streamRow(Row row) {
        return StreamSupport.stream(((Iterable<Cell>) row::cellIterator).spliterator(), false);
    }

    protected static Iterator<Row> getRowIterator(Path path, final String sheetName) {
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
}