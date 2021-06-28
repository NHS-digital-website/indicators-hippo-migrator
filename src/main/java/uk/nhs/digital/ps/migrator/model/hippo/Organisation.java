package uk.nhs.digital.ps.migrator.model.hippo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Organisation extends OrganisationImportableItem {
    private final String title;
    private final String summary;
    private final String seoSummary;
    private final String shortSummary;
    private final String parentOrganisation;
    private final String abbreviation;
    private final String synonyms;
    private final String topics;
    private final String telephone;
    private final String organisationType;
    private final String email;
    private final String url;
    private final String buildingLocation;
    private final String buildingName;
    private final String street;
    private final String area;
    private final String town;
    private final String county;
    private final String country;
    private final String type;
    private final String postalcode;
    private final String location;
    private final String code;
    private final String uri;

    public Organisation(final OrganisationFolder parentFolder,
                        final String title,
                        final String summary,
                        final String seoSummary,
                        final String shortSummary,
                        final String parentOrganisation,
                        final String abbreviation,
                        final String synonyms,
                        final String topics,
                        final String organisationType,
                        final String telephone,
                        final String email,
                        final String url,
                        final String buildingLocation,
                        final String buildingName,
                        final String street,
                        final String area,
                        final String town,
                        final String county,
                        final String country,
                        final String type,
                        final String postalcode,
                        final String location,
                        final String code,
                        final String uri) {
        super(parentFolder, title);
        this.title = title;
        this.summary = summary;
        this.seoSummary = seoSummary;
        this.shortSummary = shortSummary;
        this.parentOrganisation = parentOrganisation;
        this.abbreviation = abbreviation;
        this.synonyms = synonyms;
        this.topics = topics;
        this.organisationType = organisationType;
        this.telephone = telephone;
        this.email = email;
        this.url = url;
        this.buildingLocation = buildingLocation;
        this.buildingName = buildingName;
        this.street = street;
        this.area = area;
        this.town = town;
        this.county = county;
        this.country = country;
        this.type = type;
        this.postalcode = postalcode;
        this.location = location;
        this.code = code;
        this.uri = uri;

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

    public String getParentOrganisation() {
        return parentOrganisation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public List<String> getSynonymsList() {
        if (synonyms.isEmpty())
            return Collections.emptyList();

        final String[] synonym = synonyms.trim().split(";");
        return Arrays.asList(synonym);
    }

    public String getTopics() {
        return topics;
    }

    public List<String> getTopicsList() {
        if (topics.isEmpty())
            return Collections.emptyList();

        final String[] topic = topics.trim().split(";");
        return Arrays.asList(topic);
    }

    public String getOrganisationType() {
        return organisationType;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getHasAddress() {
        return !buildingLocation.isEmpty() ||
                !buildingName.isEmpty() ||
                !street.isEmpty() ||
                !area.isEmpty() ||
                !town.isEmpty() ||
                !county.isEmpty() ||
                !country.isEmpty() ||
                !postalcode.isEmpty() ||
                !location.isEmpty();
    }

    public String getBuildingLocation() {
        return buildingLocation;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getStreet() {
        return street;
    }

    public String getArea() {
        return area;
    }

    public String getTown() {
        return town;
    }

    public String getCounty() {
        return county;
    }

    public String getCountry() {
        return country;
    }

    public String getType() {
        return type;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public String getLocation() {
        return location;
    }

    public String getCode() {
        return code;
    }

    public String getUri() {
        return uri;
    }

}

