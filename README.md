# NHS Digital Publication System - Migrator

Prior to going live with the new Hippo CMS based publication system, there was a need to transfer
volumes of data from legacy systems into it. This program was written to facilitate conversion
of such legacy data into a format allowing to import it using Hippo's [EXIM] interface.

This program was never intended nor designed to be a general-purpose importing mechanism and has
to be modified specifically for each new type of import. The expectation was that such imports would
be few and far between, eventually ceasing completely soon after the initial go-live. As such, its
design is less polished than it would normally be for an application that requires frequent and
long-term maintenance so expect inconsistencies in the coding style and few abstractions, resulting
from ad hoc modifications for small, one-off imports.

## Development
This program is implemented as a [Spring Boot]-based command line application.

In order to add support for a new type of import, create your own class implementing interface
`MigrationTask` referring to other classes that implement this
interface as an example. Register your class in `MigratorConfiguration.tasks` method to make it
available for execution. Task's core logic is executed through method `MigrationTask.execute` while
boolean value returned from `MigrationTask.isRequested` determines whether the task will be executed
at all (typically examining arguments provided from command line).     

If your import requires some command line parameters (typically it will need at
least a path to where the source data can be read from), add them to `ExecutionParameters` and
initialise them via `ExecutionConfigurator`; see `getArgumentsDescriptors` and `getFlagsDescriptors`
in the latter for adding usage info available to be printed in console. Note that there are two
types of command line options - flags that toggle features or tasks and arguments that pass values
into the program. See how other task classes use the execution parameters.  

If this is your first contact with this project, having checked `master` branch out, execute
`make init`. This only needs to be done once and it configures your local repo for git.

Before you make a pull request, make sure to:
* Have all your individual commits squashed into one.
* Have your branch rebased onto latest master.
* Have first line of the commit's comment reference the JIRA story that given commit pertains to; make the like follow
  the format `RPS-123 shortened story title here` and not exceed 60 characters.
* Optionally, follow the first line with one blank line and a summary of the changes covered by the commit. Individual
  lines of the summary should not be longer than 72 characters.

## Migration Process Overview
Migration can be performed in two ways.

The easier way, suitable for small numbers of documents, involves:
* generating the EXIM JSON files by running this application,
* importing these files into local instance via [EXIM],
* exporting the freshly imported content using XML Export in Console,
* importing the just exported content into remote environment using XML Import in Console.

A more involved way, needed for large numbers of documents, involves:
* generating the EXIM JSON files by running this application, using `--generateImportPackage`
  to generate a single ZIP files containing these files.
* placing the ZIP file in a location where they can be downloaded by code running on the remote
  server, e.g. in a public S3 bucket.
* running a modified [EXIM] script, one that is able to download the ZIP file into temporary
  space on the remote server, unpack it and import the JSON files it includes.
  
Note that the 'modified script' mentioned in the last point above actually refers to two components:
an [Updated Editor] Groovy import script and a Java class that actually handles the download and
unzipping. The need for the Java class comes from the fact that [Updater Editor] disallows importing
a number of standard Java classes related to file and networking operations from within Groovy
script. The script and the supporting Java class used to be part of the main project but have since
been removed; should you need to use this approach, you need to restore them and deploy on the
target environment. See commit https://github.com/NHS-digital-website/hippo/commit/14af0190f40b5efb63ca099541c69f629144562c
that removed them (corresponding JIRA story: https://jira.digital.nhs.uk/browse/RPS-448).    
 
## Using the Migrator application
Migrator application can be executed either via Maven plugin or as a standalone JAR file. The former method is most
useful during development as it offers the shorterst 'make a change -> see its effect' cycle.

### Via Maven plugin
To excute it as a Maven plugin, within terminal window navigate to the root directory of the project (parent directory
of this module) and execute:
```bash
mvn spring-boot:run
```
This will assemble and execute the application which will display short usage info and quit. Only when options mentioned
in the info are specified, will the application actually try to perform some actions. When running the Migrator via
[Spring Boot Maven Plugin], the arguments have to be supplied as one, comma-separated string via
`-Drun.arguments=`; for example, to have the application decompress Nesstar ZIP export file and generate
Hippo-consumable files, one would execute:
```bash
mvn spring-boot:run -Drun.arguments="--nesstarUnzipFrom=/tmp/MigrationPackage_2017_09_12_1706.zip,--nesstarConvert"
```

### As a JAR file
In order to run the application as a standalone JAR, first you need to generate it; the following will generate JAR file
under module's `target` directory:
```bash
mvn package
```
Then you can run it as a normal Java application. To do so, from the directory where the JAR file exists, execute:
```bash
java -jar publication-system-migrator.jar
```
In this mode arguments are specified as follows:
```bash
java -jar publication-system-migrator.jar --nesstarUnzipFrom=/tmp/MigrationPackage_2017_09_12_1706.zip --nesstarConvert
```

### Example command line parameters
Individual steps of the conversion can be toggled on and off, depending on parameters provided. The intent was to
allow for execution of just selected steps during development of the Migrator in order to speed up the development
cycle.

The following paragraph present examples of certain command line parameters but keep in mind that
one the relevant imports have been executed, the examples are no longer relevant and can only be
used as illustration rather than for actual execution. 

As, in the case of Clinical Indicators content this resulted in a considerable number of parameters required to be
defined, for convenience, the following provides an example of a command line with a complete list of arguments required
to execute all steps of the conversion in one go:
```bash
java -jar publication-system-migrator.jar \
--nesstarUnzipFrom=/home/dev/migration/MigrationPackage_2018_01_15_1354.zip,\
--nesstarForceUnzip,\
--nesstarConvert,\
--nesstarAttachmentDownloadFolder=/home/dev/migration/nesstar-downloads,\
--nesstarCompendiumMappingFile=/home/dev/migration/Compendium Portal Migration - P code mapping v0.3.xlsx,\
--nesstarFieldMappingImportPath=/home/dev/migration/Coverage & Granularity for RPS - P code mapping v0.1.xlsx,\
--taxonomyDefinitionImportPath=/home/dev/migration/Taxonomy - CI Agreed v1.4 - with alphabetic list.xlsx,\
--taxonomyMappingImportPath=/home/dev/migration/Taxonomy for RPS - P code mapping v0.1.xlsx
```
Note that, while `nesstarAttachmentDownloadFolder` is optional, it's useful to point it to a location other than the
default one under `/tmp`, so that the downloaded files would be preserved between OS sessions, thus avoiding having to
spend time re-downloading them repeatedly.

Files included in the command line above are latest at the moment of writing; for their latest versions go to Confluence
page titled [Structure Mapping].


In the case of National Indicator Library content:
```bash
java -jar publication-system-migrator.jar \
--nationalIndicatorImportPath=/home/dev/migration/Indicators.xlsx,\
--nationalIndicatorAttachmentPath=/home/dev/migration/PDFpacks,\
--taxonomyDefinitionImportPath=/home/dev/migration/Taxonomy.xlsx,\
--generateImportPackage"
```

### Output

#### Execution parameters summary
At the end of successful execution Migrator will display a summary of actual execution parameters used.

#### JSON EXIM files
The program generates a number of JSON files, one file per item to be imported - one for each folder, publication, dataset and
so on. File names are prefixed with a zero-padded sequential number generated in the order the files were generated. The
idea is to create a file for parent folders before generating files for content to go into these folders. The
[import script] then reads the files in the same order to ensure that folders are created before the files.

In the case of Clinical Indicators content, while the JSON files are generated, whenever it is
discovered that corresponding Dataset contains attachments, The attachement files are downloaded and
stored locally.

Once all the JSON files have been generated and attachments downloaded, all this content is then compressed into
a single ZIP file (under directory pointed to by `importPackageDir`).

Note that since the original version of the program was written and the initial imports executed,
document types have been switched from using embedded resources to relying on S3 integration and
so the task classes used in those early imports only implement support for embedded resources.    

#### Migration report
Upon completing the conversion, a report is generated in the format of Excel spreadsheet, logging any issues that may
require attention or an intervention of a human operator. Path to the report is displayed when the program finishes
and is pointed to by execution parameter `migrationReportFilePath`.

Note that the report generating mechanism was originally developed when this application was only
being used for converting Clinical Indicators content. While the application was since extended to
support NIL and GDPR data, the report has not been updated and it remains strictly Clinical
Indicators specific. 

## Importing converted content locally
Having logged into CMS as an administrator, navigate to `Admin > Updater Editor` and click `Registry > EximImport`
script. In parameters section, make sure that `sourceBaseFolderPath` points to where the EXIM JSON files are located.
This location is reported at the end of conversion in Migrator's console through value `hippoImportDir`.

Keep `Batch Size`  set to `1` to ensure that folder files are correctly processed; the reason is that EXIM appears to only commit
changes between individual batches and if parent and child folders are created in the same batch, the parent cannot be
found when the child is being created. Keeping batch size set to `1` ensures that changes from each import file are committed
before the next file is processed.

Click `Execute` to trigger the import. At the end examine the log presented in the [Updater Editor] and application log
file as not all exceptions bubble up to the editor's log.

## GDPR import
See [GDPR import readme](gdpr-import.md) for how to import `gdprtransparency` documents.  

[Clinical Indicators portal]:   https://indicators.hscic.gov.uk/webview/
[GOSS]:                         https://www.gossinteractive.com/content-management
[Spring Boot]:                  https://projects.spring.io/spring-boot/
[Spring Boot Maven Plugin]:     https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html
[EXIM]:                         https://onehippo-forge.github.io/content-export-import/index.html
[Updater Editor]:               https://www.onehippo.org/library/concepts/update/using-the-updater-editor.html
[Groovy]:                       http://groovy-lang.org/
[import script]:                https://github.com/NHS-digital-website/hippo/blob/master/repository-data/application/src/main/resources/hcm-config/configuration/update/EximImport.groovy
[Console]:                      https://www.onehippo.org/library/concepts/content-repository/using-the-console.html
[structure mapping]:            https://confluence.digital.nhs.uk/display/CW/Structure+Mapping
[goss-hippo-migrator]:          https://github.com/NHS-digital-website/goss-hippo-migrator