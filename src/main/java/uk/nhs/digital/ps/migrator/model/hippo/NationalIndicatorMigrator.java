package uk.nhs.digital.ps.migrator.model.hippo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.Row;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import static org.apache.commons.io.FilenameUtils.removeExtension;

/**
 * This class is responsible for loading national indicators to import into hippo.
 */
public class NationalIndicatorMigrator {

    private ExecutionParameters executionParameters;
    private XlsxReader xlsxReader;

    private static final String INDICATORS_SHEET_NAME = "Indicators-Source";

    private static final String IAPCODE_COLUMN = "IAPCode";
    private static final String TITLE_COLUMN = "Title";
    private static final String PUBLISHEDBY_COLUMN = "Published By";  
    private static final String REPORTINGPERIOD_COLUMN = "Reporting Period";   
    private static final String REPORTINGLEVELS_COLUMN = "Reporting level(s)";  
    private static final String BASEDONDATAFROM_COLUMN = "Based on data from";  
    private static final String CONTACTAUTHORNAME_COLUMN = "Contact Author Name"; 
    private static final String CONTACTAUTHOREMAIL_COLUMN = "Contact Author Email";     
    private static final String RATING_COLUMN = "Rating";     
    private static final String ASSURANCEDATE_COLUMN = "Assurance date";   
    private static final String REVIEWDATE_COLUMN = "Review date";       
    private static final String INDICATORSET_COLUMN = "Indicator Set";  
    private static final String PURPOSE_COLUMN = "Purpose";
    private static final String BRIEFDESCRIPTION_COLUMN = "Brief Description";
    private static final String DEFINITION_COLUMN = "Definition";
    private static final String DATASOURCE_COLUMN = "Data Source";  
    private static final String NUMERATOR_COLUMN = "Numerator";  
    private static final String DENOMINATOR_COLUMN = "Denominator";  
    private static final String CALCULATION_COLUMN = "Calculation";   
    private static final String METHODOLOGY_COLUMN = "Methodology";                  
    private static final String INTERPRETATIONGUIDELINES_COLUMN = "Interpretation Guidelines";  
    private static final String CAVEATS_COLUMN = "Caveats";  
    private static final String TAXONOMY_COLUMN = "Taxonomy";  
    private static final String GEOGRAPHICCOVERAGE_COLUMN = "Geographical Coverage";     
    private static final String TECHNICALSPECIFICATION_COLUMN = "Technical Specification";   
    private static final String QUALITYSTATEMENT_COLUMN = "Quality Statement";    

    public NationalIndicatorMigrator(ExecutionParameters executionParameters, XlsxReader xlsReader) {
        this.executionParameters = executionParameters;
        this.xlsxReader = xlsReader;
    }

    public List<NationalIndicatorHippoImportableItem> readIndicators(){

        List<NationalIndicatorHippoImportableItem> indicators = new ArrayList<NationalIndicatorHippoImportableItem>();

        Path indicatorImportPath = executionParameters.getNationalIndicatorImportPath();
        if (indicatorImportPath == null) {
            throw new RuntimeException("There is no indicator import path.");
        }

        Iterator<Row> rowIterator = this.xlsxReader.initAndGetRenamingRowIterator(indicatorImportPath, INDICATORS_SHEET_NAME);

        //Build up collection of National Indicators. We do not specify a root folder as it is expected that one already exists ("national-indicator-library"), see NationalIndicatorHippoImportableItem.getRootPathPrefix()
       
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (null == this.xlsxReader.getCellValue(IAPCODE_COLUMN,row)){
                break;//if the spreadsheet has been saved by blank rows, break out at first occurance
            }

            indicators.add(new NationalIndicator(null,
                this.xlsxReader.getCellValue(TITLE_COLUMN,row),
                this.xlsxReader.getCellValue(IAPCODE_COLUMN,row),
                this.xlsxReader.getCellValue(TITLE_COLUMN,row),
                this.xlsxReader.getCellValue(PUBLISHEDBY_COLUMN,row),
                this.xlsxReader.getCellValue(REPORTINGPERIOD_COLUMN,row),
                this.xlsxReader.getCellValue(REPORTINGLEVELS_COLUMN,row),
                this.xlsxReader.getCellValue(BASEDONDATAFROM_COLUMN,row),
                this.xlsxReader.getCellValue(CONTACTAUTHORNAME_COLUMN,row),
                this.xlsxReader.getCellValue(CONTACTAUTHOREMAIL_COLUMN,row),                
                this.xlsxReader.getCellValue(RATING_COLUMN,row),
                this.xlsxReader.getDateValue(ASSURANCEDATE_COLUMN,row),
                this.xlsxReader.getDateValue(REVIEWDATE_COLUMN,row),
                this.xlsxReader.getCellValue(INDICATORSET_COLUMN,row),
                this.xlsxReader.getCellValueAsHtmlParagraph(PURPOSE_COLUMN,row),
                this.xlsxReader.getCellValue(BRIEFDESCRIPTION_COLUMN,row),
                this.xlsxReader.getCellValueAsHtmlParagraph(DEFINITION_COLUMN,row),
                this.xlsxReader.getCellValueAsHtmlParagraph(DATASOURCE_COLUMN,row),
                this.xlsxReader.getCellValueAsHtmlParagraph(NUMERATOR_COLUMN,row),
                this.xlsxReader.getCellValueAsHtmlParagraph(DENOMINATOR_COLUMN,row),
                this.xlsxReader.getCellValueAsHtmlParagraph(CALCULATION_COLUMN,row),   
                this.xlsxReader.getCellValueAsHtmlParagraph(METHODOLOGY_COLUMN,row),                                                    
                this.xlsxReader.getCellValueAsHtmlParagraph(INTERPRETATIONGUIDELINES_COLUMN,row),
                this.xlsxReader.getCellValueAsHtmlParagraph(CAVEATS_COLUMN,row),
                this.xlsxReader.getCellValue(TAXONOMY_COLUMN,row),
                this.xlsxReader.getCellValue(GEOGRAPHICCOVERAGE_COLUMN,row),
                this.xlsxReader.getCellValue(QUALITYSTATEMENT_COLUMN,row),
                this.xlsxReader.getCellValue(TECHNICALSPECIFICATION_COLUMN,row),
                getAttachments(executionParameters,this.xlsxReader.getCellValue(IAPCODE_COLUMN,row))
                ));
        }

        return indicators;
    }

    /*
     * Load all attachments from the NIL attachment path within a folder named with the IAP code. Gracefully handles
     * non-existing folders as this is a valid use-case.
     */
    private List<Attachment> getAttachments(ExecutionParameters executionParameters, String iapCode) {
        List<Attachment> attachments = new ArrayList<Attachment>();
        
        try (Stream<Path> stream = Files.walk(Paths.get(executionParameters.getNationalIndicatorAttachmentPath() + "/" + iapCode))){
			attachments = stream
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .map(f -> new Attachment(
                    executionParameters.getNationalIndicatorAttachmentPath(),
                    removeExtension(f.getName()),
                    "/" + iapCode + "/" + f.getName(),
                    null)).collect(Collectors.toList());
		} catch (IOException e) {
			// we don't care if the path doesn't exist 
        }
        return attachments;
    }
}