package uk.nhs.digital.ps.migrator.report;

public enum DatasetMigrationImpact {

    FIELD_MIGRATED_AS_IS("Field migrated as is"),
    FIELD_NOT_MIGRATED("Field not migrated"),
    FIELD_MIGRATED_PARTIALLY("Field migrated partially"),
    VALUE_MIGRATED_MODIFIED("Modified value migrated"),
    DATASET_NOT_MIGRATED("Dataset not migrated"),
    ONE_DUPLICATE_DATASET_MIGRATED("Only one duplicate migrated");

    private final String description;

    DatasetMigrationImpact(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
