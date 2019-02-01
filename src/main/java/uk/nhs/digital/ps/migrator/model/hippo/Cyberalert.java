package uk.nhs.digital.ps.migrator.model.hippo;

import static java.util.stream.Collectors.toList;

import org.apache.commons.lang3.StringUtils;
import uk.nhs.digital.ps.migrator.misc.TextHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Cyberalert extends CyberalertImportableItem {

    private final Map<String, String> mapPlatformToUuid;

    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Europe/London");
    private static final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        DATE_FORMAT.setTimeZone(TIME_ZONE);
    }

    private final String title;
    private final String summary;
    private final String seoSummary;
    private final String shortSummary;
    private final String threatId;
    private final String threatSeverity;
    private final String threatCategory;
    private final String threatType;
    private final String threatVector;
    private final String datePublished;
    private final String dateLastUpdated;
    private final String services;
    private final String topics;
    private final String platformText;
    private final String platformAffected;
    private final String versionsAffected;
    private final String threatHeader;
    private final String threatDetail;
    private final String updatePublished1;
    private final String updateHeader1;
    private final String updateDetail1;
    private final String remediationIntroduction;
    private final String step;
    private final String remediationAction;
    private final String remediationType;
    private final String indicatorsOfCompromise;
    private final String linkToNcsc;
    private final String definitiveSourceOfThreatUpdates;
    private final String cveIdentifier;
    private final String cveText;
    private final String cveStatus;
    private final String publicallyAccessible;


    public Cyberalert( final Map<String, String> mapPlatformToUuid,
                       final CyberalertFolder parentFolder,
                       final String title,
                       final String summary,
                       final String seoSummary,
                       final String shortSummary,
                       final String threatId,
                       final String threatSeverity,
                       final String threatCategory,
                       final String threatType,
                       final String threatVector,
                       final String datePublished,
                       final String dateLastUpdated,
                       final String services,
                       final String topics,
                       final String platformText,
                       final String platformAffected,
                       final String versionsAffected,
                       final String threatHeader,
                       final String threatDetail,
                       final String updatePublished1,
                       final String updateHeader1,
                       final String updateDetail1,
                       final String remediationIntroduction,
                       final String step,
                       final String remediationAction,
                       final String remediationType,
                       final String indicatorsOfCompromise,
                       final String linkToNcsc,
                       final String definitiveSourceOfThreatUpdates,
                       final String cveIdentifier,
                       final String cveText,
                       final String cveStatus,
                       final String publicallyAccessible) {
        super(parentFolder, threatId);

        this.mapPlatformToUuid = mapPlatformToUuid;
        this.title = title;
        this.summary = summary;
        this.seoSummary = seoSummary;
        this.shortSummary = shortSummary;
        this.threatId = threatId;
        this.threatSeverity = threatSeverity;
        this.threatCategory = threatCategory;
        this.threatType = threatType;
        this.threatVector = threatVector;
        this.datePublished = datePublished;
        this.dateLastUpdated = dateLastUpdated;
        this.services = services;
        this.topics = topics;
        this.platformText = platformText;
        this.platformAffected = platformAffected;
        this.versionsAffected = versionsAffected;
        this.threatHeader = threatHeader;
        this.threatDetail = threatDetail;
        this.updatePublished1 = updatePublished1;
        this.updateHeader1 = updateHeader1;
        this.updateDetail1 = updateDetail1;
        this.remediationIntroduction = remediationIntroduction;
        this.step = step;
        this.remediationAction = remediationAction;
        this.remediationType = remediationType;
        this.indicatorsOfCompromise = indicatorsOfCompromise;
        this.linkToNcsc = linkToNcsc;
        this.definitiveSourceOfThreatUpdates = definitiveSourceOfThreatUpdates;
        this.cveIdentifier = cveIdentifier;
        this.cveText = cveText;
        this.cveStatus = cveStatus;
        this.publicallyAccessible = publicallyAccessible;
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

    public String getThreatId() {
        return threatId;
    }

    public String getThreatSeverity() {
        return threatSeverity;
    }

    public String getThreatCategory() {
        return formatToMultiple(threatCategory);

    }

    public String getThreatType() {
        return threatType;
    }

    public String getThreatVector() {
        return formatToMultiple(threatVector);
    }

    private String formatToMultiple(String input) {
        StringJoiner joiner = new StringJoiner("\",\"","\"","\"");
        String[] arr = input.split(";");
        for (String s : arr) {
            joiner.add(s);
        }
        return joiner.toString();
    }

    public Calendar getDatePublishedCalendar() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(datePublished));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public String getDatePublishedAndTime() throws ParseException {
        return DATE_FORMAT.format(
                new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(datePublished));
    }

    public String getDateLastUpdated() {
        return dateLastUpdated;
    }

    public String getServices() {
        return services;
    }

    public String getTopics() {
        return topics;
    }

    public String getPlatformText() {
        return platformText;
    }

    public String getPlatformAffected() {
        return platformAffected;
    }

    public List<PlatformLink> getPlatforms() {

        if (platformAffected.isEmpty())
            return Collections.emptyList();

        return Arrays.stream(platformAffected.split(";"))
                .map(this::createPlatform)
                .collect(toList());
    }

    private PlatformLink createPlatform(String text) {

        String name = TextHelper.normaliseToJcrPathName(text);
        if (mapPlatformToUuid.containsKey(name)) {
            return new PlatformLink(mapPlatformToUuid.get(name), name);
        } else {
            System.out.println("NOT FOUND-->" + name);
            return null;
        }
    }

    public List<String> getCveIdentifiers() {

        if (cveIdentifier.isEmpty())
            return Collections.emptyList();

        final String[] cve = cveIdentifier.trim().split(";");
        return Arrays.asList(cve);
    }

    public String getVersionsAffected() {
        return versionsAffected;
    }

    public String getThreatHeader() {
        return threatHeader;
    }

    public String getThreatDetail() {
        return threatDetail;
    }

    public String getUpdatePublished1() {
        return updatePublished1;
    }

    public String getUpdatePublished1AndTime() throws ParseException {
        return DATE_FORMAT.format(
                new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(updatePublished1));
    }

    public String getUpdateHeader1() {
        return updateHeader1;
    }

    public String getUpdateDetail1() {
        return updateDetail1;
    }

    public String getRemediationIntroduction() {
        return remediationIntroduction;
    }

    public String getStep() {
        return step;
    }

    public String getRemediationAction() {
        return remediationAction;
    }

    public String getRemediationType() {
        return remediationType;
    }

    public String getIndicatorsOfCompromise() {
        return indicatorsOfCompromise;
    }

    public String getLinkToNcsc() {
        return linkToNcsc;
    }

    public String getDefinitiveSourceOfThreatUpdates() {
        return definitiveSourceOfThreatUpdates;
    }

    public String getCveIdentifier() {
        return cveIdentifier;
    }

    public String getCveText() {
        return cveText;
    }

    public String getCveStatus() {
        return cveStatus;
    }

    public String getPublicallyAccessible() {
        return publicallyAccessible.toLowerCase();
    }

    public class PlatformLink {

        private String id;
        private String name;

        PlatformLink(String uuid, String jcrname) {
            id = uuid;
            name = jcrname;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }


    }
}



