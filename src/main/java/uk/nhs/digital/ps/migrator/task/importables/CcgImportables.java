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

public class CcgImportables {

    private final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory;

    public CcgImportables(final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory) {
        this.clinicalIndicatorsImportableItemsFactory = clinicalIndicatorsImportableItemsFactory;
    }

    public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final CiFolder ciRootFolder) {

        // Target CMS structure:
        //
        // A)  CCG Outcomes Indicator Set                           FOLDER
        // B)    Current                                            FOLDER
        // C)      content                                          PUBLICATION
        // D)        folders per node (domain x)                    FOLDER
        // E)          DataSet documents from subfolders            DATASET
        // F)    Archive                                            FOLDER
        // G)      content                                          SERIES
        // H)      2016                                             PUBLICATION (to be created manually by editors)

        // A)
        final Catalog ccgRootCatalog = catalogStructure.findCatalogByLabel("CCG Outcomes Indicator Set");
        final CiFolder ccgRootFolder = clinicalIndicatorsImportableItemsFactory.toFolder(ciRootFolder, ccgRootCatalog);

        // B)
        final CiFolder currentPublicationFolder = clinicalIndicatorsImportableItemsFactory.newFolder(ccgRootFolder, "Current");


        // D)
        // There is only one level of Domains under CCG so it's enough to just iterate over them rather than walk a tree
        final List<Catalog> domainCatalogs = ccgRootCatalog.getChildCatalogs();

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
            ccgRootFolder.getLocalizedName(),
            domainsWithDatasets);

        // F)
        final CiFolder archiveFolder = clinicalIndicatorsImportableItemsFactory.newFolder(ccgRootFolder, "Archive");

        // G
        final Archive archive = clinicalIndicatorsImportableItemsFactory.newArchive(archiveFolder, ccgRootFolder.getLocalizedName());

        final List<HippoImportableItem> importableItems = new ArrayList<>();
        importableItems.add(ccgRootFolder);
        importableItems.add(currentPublicationFolder);
        importableItems.add(currentPublication);
        importableItems.addAll(domainsWithDatasets);
        importableItems.add(archiveFolder);
        importableItems.add(archive);

        return importableItems;
    }
}
