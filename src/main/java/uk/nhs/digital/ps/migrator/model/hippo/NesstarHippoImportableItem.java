package uk.nhs.digital.ps.migrator.model.hippo;

public class NesstarHippoImportableItem extends HippoImportableItem {

    protected NesstarHippoImportableItem(final NesstarHippoImportableItem parentFolder,
        final String localizedName
    ) {
        super(parentFolder, localizedName);
    }

    protected String getRootPathPrefix(){
        return ROOT_PATH_PREFIX + "publication-system";
    }
}
