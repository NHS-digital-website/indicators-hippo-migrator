package uk.nhs.digital.ps.migrator.model.hippo;

public class NationalIndicator extends NationalIndicatorHippoImportableItem {

    private final String title;
    private final String summary;

    public NationalIndicator(final NilFolder parent,
                       final String displayName,
                       final String title) {
        super(parent, displayName);
        this.title = title;
        this.summary = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return title + " Summary";
    }
}
