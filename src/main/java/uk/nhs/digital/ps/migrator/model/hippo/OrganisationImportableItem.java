package uk.nhs.digital.ps.migrator.model.hippo;

public class OrganisationImportableItem extends HippoImportableItem {

    OrganisationImportableItem(final HippoImportableItem parentFolder, final String localizedName) {
        super(parentFolder, localizedName);
    }

    @Override
    protected String getRootPathPrefix() {
        return ROOT_PATH_PREFIX + "organisation";
    }
}
