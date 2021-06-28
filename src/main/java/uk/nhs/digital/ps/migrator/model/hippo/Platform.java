package uk.nhs.digital.ps.migrator.model.hippo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Platform extends PlatformImportableItem {
    private final String title;
    private final String summary;
    private final String seoSummary;
    private final String shortSummary;
    private final String platformType;
    private final String abbreviation;
    //private final String synonyms;
    private final String versionNumber;
    private final String versionStatus;
    private final String versionUrl;
    private final String supplierName;
    private final String supplierUuid;
    private final String resellerName;
    private final String resellerUuid;
    private final String platformUrl;
    private final String topics;


    public Platform(final PlatformFolder parentFolder,
                    final String title,
                    final String summary,
                    final String seoSummary,
                    final String shortSummary,
                    final String platformType,
                    final String abbreviation,
                    //final String synonyms,
                    final String versionNumber,
                    final String versionStatus,
                    final String versionUrl,
                    final String supplierName,
                    final String supplierUuid,
                    final String resellerName,
                    final String resellerUuid,
                    final String platformUrl,
                    final String topics) {
        super(parentFolder, title);
        this.title = title;
        this.summary = summary;
        this.seoSummary = seoSummary;
        this.shortSummary = shortSummary;
        this.platformType = platformType;
        this.abbreviation = abbreviation;
        //this.synonyms = synonyms;
        this.versionNumber = versionNumber;
        this.versionStatus = versionStatus;
        this.versionUrl = versionUrl;
        this.supplierName = supplierName;
        this.supplierUuid = supplierUuid;
        this.resellerName = resellerName;
        this.resellerUuid = resellerUuid;
        this.platformUrl = platformUrl;
        this.topics = topics;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getSeoSummary() {
        return seoSummary;
    }

    public String getShortSummary() {
        return shortSummary;
    }

    public String getPlatformType() {
        return platformType;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public List<String> getAbbreviationList() {
        if (abbreviation.isEmpty())
            return Collections.emptyList();

        final String[] abbr = abbreviation.trim().split(";");
        return Arrays.asList(abbr);
    }

//    public String getSynonyms() {
//        return synonyms;
//    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierUuid() {
        return supplierUuid;
    }

    public String getResellerName() {
        return resellerName;
    }

    public String getResellerUuid() {
        return resellerUuid;
    }

    public String getPlatformUrl() {
        return platformUrl;
    }

    public String getTopics() {
        return topics;
    }

}

