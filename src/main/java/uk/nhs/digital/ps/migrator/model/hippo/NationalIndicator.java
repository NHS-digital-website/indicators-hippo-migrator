package uk.nhs.digital.ps.migrator.model.hippo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import uk.nhs.digital.ps.migrator.model.taxonomy.TaxonomyTerm;

public class NationalIndicator extends NationalIndicatorHippoImportableItem {

    private final String iapCode;
    private final String title;
    private final String publishedBy;
    private final String reportingPeriod;
    private final String reportingLevel;
    private final String basedOn;
    private final String contactAuthorName;
    private final String contactAuthorEmail;    
    private final String rating;
    private final String assuranceDate;
    private final String reviewDate;
    private final String indicatorSet;
    private final String purpose;
    private final String briefDescription;    
    private final String definition;
    private final String dataSource;
    private final String numerator;
    private final String denominator;
    private final String calculation;  
    private final String methodology;               
    private final String interpretationGuidelines;
    private final String caveats;
    private final String taxonomyKeys;
    private final String geographicCoverage; 
    private final List<Attachment> attachments;
    private final String qualityStatementUrl; 
    private final String technicalSpecificationUrl;    

    public NationalIndicator(final NilFolder parent,
                       final String displayName,
                       final String iapCode,
                       final String title,
                       final String publishedBy,
                       final String reportingPeriod,
                       final String reportingLevel,
                       final String basedOn,
                       final String contactAuthorName,
                       final String contactAuthorEmail,
                       final String rating,
                       final String assuranceDate,
                       final String reviewDate,
                       final String indicatorSet,
                       final String purpose,
                       final String briefDescription,                       
                       final String definition,
                       final String dataSource,
                       final String numerator,
                       final String denominator,
                       final String calculation,
                       final String methodology,
                       final String interpretationGuidelines,
                       final String caveats,
                       final String taxonomyKeys,
                       final String geographicCoverage,
                       final String qualityStatementUrl,
                       final String technicalSpecificationUrl,
                       final List<Attachment> attachments) {
        super(parent, displayName);
        this.iapCode = iapCode;
        this.title = title;
        this.publishedBy = publishedBy;
        this.reportingPeriod = reportingPeriod;
        this.reportingLevel = reportingLevel;
        this.basedOn = basedOn;
        this.contactAuthorName = contactAuthorName;
        this.contactAuthorEmail = contactAuthorEmail;
        this.rating = rating;
        this.assuranceDate = assuranceDate;
        this.reviewDate = reviewDate;
        this.indicatorSet = indicatorSet;
        this.purpose = purpose;
        this.briefDescription = briefDescription;
        this.definition = definition;
        this.dataSource = dataSource;
        this.numerator = numerator;
        this.denominator = denominator;
        this.calculation = calculation;
        this.methodology = methodology;
        this.interpretationGuidelines = interpretationGuidelines;
        this.caveats = caveats;
        this.geographicCoverage = geographicCoverage;
        this.qualityStatementUrl = qualityStatementUrl;
        this.technicalSpecificationUrl = technicalSpecificationUrl;

        //split the string on any ',' that is followed by an even number of double quotes, i.e. handle quoted taxonomy terms
        List<String> taxKeys = Arrays.asList(taxonomyKeys.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

        List<String> convertedKeys = taxKeys.stream() // Convert collection to Stream
				.map(TaxonomyTerm::covertTermToKey) // Convert each term to the sanitised taxonomy key
                .collect(Collectors.toList()); // Collect results to a new list
                
        this.taxonomyKeys = String.join("\", \"", convertedKeys);

        this.attachments = attachments;
    }

    public String getIapCode() {
        return iapCode;
    }

    public String getTitle() {
        return title;
    }

	public String getPublishedBy() {
		return publishedBy;
	}

	public String getReportingPeriod() {
		return reportingPeriod;
	}

	public String getReportingLevel() {
		return reportingLevel;
	}

	public String getbasedOn() {
		return basedOn;
	}

	public String getContactAuthorName() {
		return contactAuthorName;
    }
    
	public String getContactAuthorEmail() {
		return contactAuthorEmail;
	}    

	public String getRating() {
		return rating;
	}

	public String getAssuranceDate() {
		return assuranceDate;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public String getIndicatorSet() {
		return indicatorSet;
	}

	public String getPurpose() {
		return purpose;
	}

	public String getBriefDescription() {
		return briefDescription;
	}

	public String getDefinition() {
		return definition;
	}

	public String getDataSource() {
		return dataSource;
	}

	public String getNumerator() {
		return numerator;
    }
    
	public String getDenominator() {
		return denominator;
    }
    
	public String getCalculation() {
		return calculation;
	}    

	public String getMethodology() {
		return methodology;
	}    

	public String getInterpretationGuidelines() {
		return interpretationGuidelines;
	}

	public String getCaveats() {
		return caveats;
    }
    
	public String getTaxonomyKeys() {
		return taxonomyKeys;
    }    
    
	public String getGeographicCoverage() {
		return geographicCoverage;
    }  
    
	public String getQualityStatementUrl() {
		return qualityStatementUrl;
    }  

	public String getTechnicalSpecificationUrl() {
		return technicalSpecificationUrl;
    }  

    public List<Attachment> getAttachments() {
        return attachments;
    }    
}
