package uk.nhs.digital.ps.migrator.task.importables;

import static java.util.stream.Collectors.toList;

import uk.nhs.digital.ps.migrator.model.hippo.Archive;
import uk.nhs.digital.ps.migrator.model.hippo.CiFolder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;
import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.CatalogStructure;
import uk.nhs.digital.ps.migrator.task.ClinicalIndicatorsImportableItemsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SocialCareImportables {

    private final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory;

    public SocialCareImportables(final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory) {
        this.clinicalIndicatorsImportableItemsFactory = clinicalIndicatorsImportableItemsFactory;
    }

    public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final CiFolder ciRootFolder) {

        // Target CMS structure:
        //
        // A)  Adult Social Care Outcomes Framework (ASCOF)         FOLDER
        // B)    Current                                            FOLDER
        // C)      content                                          PUBLICATION
        // D)        folders per node (domain x)                    FOLDER
        // E)          DataSet documents from subfolders            DATASET
        // F)    Archive                                            FOLDER
        // G)      content                                          SERIES
        // H)      2016                                             PUBLICATION (to be created manually by authors)

        // A)
        final Catalog rootCatalog = catalogStructure.findCatalogByLabel("Adult Social Care Outcomes Framework (ASCOF)");
        final CiFolder rootFolder = clinicalIndicatorsImportableItemsFactory.toFolder(ciRootFolder, rootCatalog);

        // B)
        final CiFolder currentPublicationFolder = clinicalIndicatorsImportableItemsFactory.newFolder(rootFolder, "Current");

        // D)
        final List<Catalog> domainCatalogs = rootCatalog.getChildCatalogs();

        final List<HippoImportableItem> domainsWithDatasets = domainCatalogs.stream()
            .flatMap(domainCatalog -> {
                final CiFolder domainFolder = clinicalIndicatorsImportableItemsFactory.toFolder(currentPublicationFolder, domainCatalog);

                return Stream.concat(
                    Stream.of(domainFolder),
                    // E)
                    domainCatalog.findPublishingPackages().stream().map(domainPublishingPackage ->
                        clinicalIndicatorsImportableItemsFactory.toDataSet(domainFolder, domainPublishingPackage)
                    )
                );

            }).collect(toList());

        // C)
        final Publication currentPublication = clinicalIndicatorsImportableItemsFactory.newPublication(
            currentPublicationFolder,
            "content",
            rootFolder.getLocalizedName(),
            domainsWithDatasets);

        // F)
        final CiFolder archiveFolder = clinicalIndicatorsImportableItemsFactory.newFolder(rootFolder, "Archive");

        // G
        final Archive archive = clinicalIndicatorsImportableItemsFactory.newArchive(archiveFolder, rootFolder.getLocalizedName());

        final List<HippoImportableItem> importableItems = new ArrayList<>();
        importableItems.add(rootFolder);
        importableItems.add(currentPublicationFolder);
        importableItems.add(currentPublication);
        importableItems.addAll(domainsWithDatasets);
        importableItems.add(archiveFolder);
        importableItems.add(archive);

        return importableItems;
    }

}
