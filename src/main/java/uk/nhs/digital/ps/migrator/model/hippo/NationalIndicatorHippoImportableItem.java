package uk.nhs.digital.ps.migrator.model.hippo;

public class NationalIndicatorHippoImportableItem extends HippoImportableItem {

    protected NationalIndicatorHippoImportableItem(final NilFolder parentFolder,
                                                   final String localizedName
    ) {
        super(parentFolder, localizedName);
    }

    protected String getRootPathPrefix(){
        return ROOT_PATH_PREFIX + "national-indicator-library";
    }
}
