package uk.nhs.digital.ps.migrator.model.hippo;

public class GdprImportableItem extends HippoImportableItem {

    GdprImportableItem(final HippoImportableItem parentFolder, final String localizedName) {
        super(parentFolder, localizedName);
    }

    @Override
    protected String getRootPathPrefix() {
        return ROOT_PATH_PREFIX + "about-nhs-digital";
    }
}
