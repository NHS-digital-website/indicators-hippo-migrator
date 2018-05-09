package uk.nhs.digital.ps.migrator.model.hippo;

public class GdprTransparencyDocument extends GdprImportableItem {
    private final String title;
    private final String dataController;
    private final String assetRefNumber;
    private final String howUseInformation;
    private final String lawfulBasis;
    private final String sensitivity;
    private final String outsideUk;
    private final String timeRetained;
    private final String withdrawConsent;
    private final String dataSource;
    private final String computerDecision;
    private final String legallyWhy;
    private final String whoCanAccess;
    private final String rights;
    private final String summary;
    private final String seoSummary;
    private final String shortSummary;

    public GdprTransparencyDocument(final GdprFolder parentFolder,
                                    final String title,
                                    final String dataController,
                                    final String assetRefNumber,
                                    final String howUseInformation,
                                    final String lawfulBasis,
                                    final String sensitivity,
                                    final String outsideUk,
                                    final String timeRetained,
                                    final String withdrawConsent,
                                    final String dataSource,
                                    final String computerDecision,
                                    final String legallyWhy,
                                    final String whoCanAccess, String rights,
                                    final String summary,
                                    final String seoSummary,
                                    final String shortSummary) {
        super(parentFolder, title);
        this.title = title;
        this.dataController = dataController;
        this.assetRefNumber = assetRefNumber;
        this.howUseInformation = howUseInformation;
        this.lawfulBasis = lawfulBasis;
        this.sensitivity = sensitivity;
        this.outsideUk = outsideUk;
        this.timeRetained = timeRetained;
        this.withdrawConsent = withdrawConsent;
        this.dataSource = dataSource;
        this.computerDecision = computerDecision;
        this.legallyWhy = legallyWhy;
        this.whoCanAccess = whoCanAccess;
        this.rights = rights;
        this.summary = summary;
        this.seoSummary = seoSummary;
        this.shortSummary = shortSummary;
    }

    public String getTitle() {
        return title;
    }

    public String getDataController() {
        return dataController;
    }

    public String getAssetRefNumber() {
        return assetRefNumber;
    }

    public String getHowUseInformation() {
        return howUseInformation;
    }

    public String getLawfulBasis() {
        return lawfulBasis;
    }

    public String getSensitivity() {
        return sensitivity;
    }

    public String getOutsideUk() {
        return outsideUk;
    }

    public String getTimeRetained() {
        return timeRetained;
    }

    public String getWithdrawConsent() {
        return withdrawConsent;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getComputerDecision() {
        return computerDecision;
    }

    public String getLegallyWhy() {
        return legallyWhy;
    }

    public String getWhoCanAccess() {
        return whoCanAccess;
    }

    public String getRights() {
        return rights;
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
}

