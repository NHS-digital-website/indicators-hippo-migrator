package uk.nhs.digital.ps.migrator.model.hippo;

public class PlatformImportableItem extends HippoImportableItem {

    PlatformImportableItem(final HippoImportableItem parentFolder, final String localizedName) {
        super(parentFolder, localizedName);
    }

    @Override
    protected String getRootPathPrefix() {
        return ROOT_PATH_PREFIX + "platform";
    }
}
