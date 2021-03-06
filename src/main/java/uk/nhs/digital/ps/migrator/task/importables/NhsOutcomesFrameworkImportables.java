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

public class NhsOutcomesFrameworkImportables {

    private final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory;

    public NhsOutcomesFrameworkImportables(final ClinicalIndicatorsImportableItemsFactory clinicalIndicatorsImportableItemsFactory) {
        this.clinicalIndicatorsImportableItemsFactory = clinicalIndicatorsImportableItemsFactory;
    }

    public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final CiFolder ciRootFolder) {
        final List<HippoImportableItem> importableItems = new ArrayList<>();

        // Target CMS structure:
        //
        // A)  NHS Outcomes Framework                               FOLDER
        // B)    Current                                            FOLDER
        // C)      content                                          PUBLICATION
        // D)        folders per node                               FOLDER
        // E)          DataSet documents from subfolders            DATASET
        // F)    Archive                                            FOLDER
        // G)      content                                          SERIES
        // H)      2016                                             PUBLICATION (to be created manually by editors)

        // A)
        final Catalog rootCatalog = catalogStructure.findCatalogByLabel("NHS Outcomes Framework");
        final CiFolder nfoRootFolder = clinicalIndicatorsImportableItemsFactory.toFolder(ciRootFolder, rootCatalog);

        // B)
        final CiFolder currentPublicationFolder = clinicalIndicatorsImportableItemsFactory.newFolder(nfoRootFolder, "Current");

        // D)
        final List<HippoImportableItem> domainsWithDatasets = rootCatalog.getChildCatalogs()
            .stream()
            .filter(domainCatalog -> !"NHS Outcomes Framework (NHS OF) summary dashboard and useful links".equals(domainCatalog.getLabel()))
            .flatMap(domainCatalog -> {
                final CiFolder domainFolder = clinicalIndicatorsImportableItemsFactory.toFolder(currentPublicationFolder, domainCatalog);

                return Stream.concat(
                    Stream.of(domainFolder),
                    // E)
                    getImportableDatasetsFromCatalog(domainCatalog, domainFolder)
                );
            }).collect(toList());

        // C)
        final Publication currentPublication = clinicalIndicatorsImportableItemsFactory.newPublication(
            currentPublicationFolder,
            "content",
            nfoRootFolder.getLocalizedName(),
            domainsWithDatasets);

        // F)
        final CiFolder archiveFolder = clinicalIndicatorsImportableItemsFactory.newFolder(nfoRootFolder, "Archive");

        // G
        final Archive archive = clinicalIndicatorsImportableItemsFactory.newArchive(archiveFolder, nfoRootFolder.getLocalizedName());

        importableItems.add(nfoRootFolder);
        importableItems.add(currentPublicationFolder);
        importableItems.add(currentPublication);
        importableItems.addAll(domainsWithDatasets);
        importableItems.add(archiveFolder);
        importableItems.add(archive);

        return importableItems;
    }

    private Stream<HippoImportableItem> getImportableDatasetsFromCatalog(Catalog rootCatalog, CiFolder rootFolder) {
        List<Catalog> catalogs = rootCatalog.getChildCatalogs();

        if (catalogs.isEmpty()) {
            return rootCatalog.findPublishingPackages().stream().map(domainPublishingPackage ->
                clinicalIndicatorsImportableItemsFactory.toDataSet(rootFolder, domainPublishingPackage)
            );
        }

        return catalogs.stream()
            .flatMap(domainCatalog -> getImportableDatasetsFromCatalog(domainCatalog, rootFolder));
    }
}
