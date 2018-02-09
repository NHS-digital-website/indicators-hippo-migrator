package uk.nhs.digital.ps.migrator.model.hippo;

public class NationalIndicator extends NationalIndicatorHippoImportableItem {

    private final String iapCode;
    private final String title;
    private final String publishedBy;
    private final String publishedDate;
    private final String reportingPeriod;
    private final String reportingLevel;
    private final String basedOn;
    private final String contactAuthor;
    private final String rating;
    private final String assuranceDate;
    private final String reviewDate;
    private final String indicatorSet;
    private final String purpose;
    private final String definition;
    private final String descriptor;
    private final String interpretationGuidelines;
    private final String caveats;

    public NationalIndicator(final NilFolder parent,
                       final String displayName,
                       final String iapCode,
                       final String title,
                       final String publishedBy,
                       final String publishedDate,
                       final String reportingPeriod,
                       final String reportingLevel,
                       final String basedOn,
                       final String contactAuthor,
                       final String rating,
                       final String assuranceDate,
                       final String reviewDate,
                       final String indicatorSet,
                       final String purpose,
                       final String definition,
                       final String descriptor,
                       final String interpretationGuidelines,
                       final String caveats
                       ) {
        super(parent, displayName);
        this.iapCode = iapCode;
        this.title = title;
        this.publishedBy = publishedBy;
        this.publishedDate = publishedDate;
        this.reportingPeriod = reportingPeriod;
        this.reportingLevel = reportingLevel;
        this.basedOn = basedOn;
        this.contactAuthor = contactAuthor;
        this.rating = rating;
        this.assuranceDate = assuranceDate;
        this.reviewDate = reviewDate;
        this.indicatorSet = indicatorSet;
        this.purpose = purpose;
        this.definition = definition;
        this.descriptor = descriptor;
        this.interpretationGuidelines = interpretationGuidelines;
        this.caveats = caveats;
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

	public String getPublishedDate() {
		return publishedDate;
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

	public String getContactAuthor() {
		return contactAuthor;
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

	public String getDefinition() {
		return definition;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public String getInterpretationGuidelines() {
		return interpretationGuidelines;
	}

	public String getCaveats() {
		return caveats;
	}
}
