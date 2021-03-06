package uk.nhs.digital.ps.migrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import uk.nhs.digital.ps.migrator.config.ExecutionConfigurator;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.misc.Descriptor;
import uk.nhs.digital.ps.migrator.misc.MigratorExitCodeGenerator;
import uk.nhs.digital.ps.migrator.report.MigrationReport;
import uk.nhs.digital.ps.migrator.task.MigrationTask;

import java.util.List;

import static java.lang.String.format;
import static uk.nhs.digital.ps.migrator.config.ExecutionConfigurator.HELP_FLAG;

@SpringBootApplication
public class PublicationSystemMigrator implements ApplicationRunner, ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(PublicationSystemMigrator.class);

    private final List<MigrationTask> migrationTasks;

    private final ExecutionConfigurator executionConfigurator;

    private final MigrationReport migrationReport;

    private final ExecutionParameters executionParameters;

    private MigratorExitCodeGenerator migratorExitCodeGenerator;

    private ApplicationContext applicationContext;


    public static void main(final String... args) {
        SpringApplication.run(PublicationSystemMigrator.class, args);
    }

    public PublicationSystemMigrator(final List<MigrationTask> migrationTasks,
                                     final ExecutionConfigurator executionConfigurator,
                                     final MigrationReport migrationReport,
                                     final ExecutionParameters executionParameters, final MigratorExitCodeGenerator
                                             migratorExitCodeGenerator) {
        this.migrationTasks = migrationTasks;
        this.executionConfigurator = executionConfigurator;
        this.migrationReport = migrationReport;
        this.executionParameters = executionParameters;
        this.migratorExitCodeGenerator = migratorExitCodeGenerator;
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {

        if (args.getOptionNames().isEmpty() || args.containsOption(HELP_FLAG)) {
            printUsageInfo();
            return;
        }

        executionConfigurator.initExecutionParameters(args);

        logExecutionParameters();

        try {
            migrationTasks.stream()
                .peek(migrationTask -> {
                    if (!migrationTask.isRequested()) {
                        log.debug("Skipping {} - not requested", migrationTask.getClass().getSimpleName());
                    }
                })
                .filter(MigrationTask::isRequested).forEach(MigrationTask::execute);

        } catch (final Exception e) {
            migrationReport.logError(e, "Unexpected error occurred, conversion has failed.");
        } finally {

            migrationReport.generate();
            migrationReport.logErrors();

            logExecutionParameters();

            if (migratorExitCodeGenerator.isRunFailed()) {
                log.error("");
                log.error("CONVERSION  HAS FAILED; SEE THE LOG FOR ERRORS.");
                log.error("");
                System.exit(SpringApplication.exit(applicationContext));
            }
        }
    }

    private void logExecutionParameters() {
        log.info("");
        log.info("Execution parameters used:");
        executionParameters.descriptions().forEach(descriptor -> log.info("    {}: {}", descriptor.getTerm(), descriptor
            .getDescription()));
        log.info("");
    }

    private void printUsageInfo() {

        log.info("*******************************************************");
        log.info("*");
        log.info("* Program migrating content from Nesstar and GOSS into the new,");
        log.info("* Hippo CMS-based Publication System.");
        log.info("*");
        log.info("* Usage: java -jar publication-system-migrator.jar [arguments | flags]");
        log.info("*");
        log.info("* Specify the following arguments to provide values used in execution.");
        log.info("* Example: --argOne=argOneValue");
        log.info("*");
        printDescriptors(executionConfigurator.getArgumentsDescriptors());

        log.info("* ");
        log.info("* Specify the following flags (options not followed by any values) to toggle specific behaviours.");
        log.info("* Example --flagOne");
        log.info("* ");
        printDescriptors(executionConfigurator.getFlagsDescriptors());
        log.info("*");
        log.info("*******************************************************");
    }

    private void printDescriptors(final List<Descriptor> descriptors) {
        descriptors.forEach(optionDescriptor -> {
            log.info("* {}: {}",
                format("%20s", "--" + optionDescriptor.getTerm()),
                optionDescriptor.getDescription()
            );
        });
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
