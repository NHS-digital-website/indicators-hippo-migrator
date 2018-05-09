# GDPR Import

The following steps need taking in order to import `gdprtransparency` documents using spreadsheet
in the format of [GDPR-Document-Upload.xlsx](src/test/resources/GDPR-Document-Upload.xlsx).

Note that it's best to test the steps that manipulate the remote environment on one of the test
environments first, for example, UAT. Having imported the documents there, let the responsible
business user know so that they can review and approve the documents to be imported into production
environment. 

1. Verify that no change was made to the `website:gdprtransparency` document type since the last
   import and if it was, update involved components (`gdprtransparencydocument.json.ftl`,
   `GdprTransparencyDocument`, `GdprTransparencyConversionTask` as required. 
1. **Have a local development instance of Hippo running**, ensuring it's of the same version as
   deployed on the target remote environment.
   - Imports expects that folder _Corporate Website > About NHS Digital_ exist and creates temporary
     folder _GDPR Import_ folder in it, importing the documents into the latter. Path of the parent
     folder can be changed by editing `GdprImportableItem#getRootPathPrefix` if required.
1. Run converter to **transform spreadsheet into JSON files**.
   - GDPR conversion requires that the following command line arguments are specified:
     - `--gdprSrcWorkbookPath`: Path to the workbook with content of GDPR documents to convert to
       [EXIM]-compatible JSON files.
     - `--gdprSrcSpreadsheetName`: Name of the spreadsheed defining GDPR document to import from
       workbook given by `--gdprSrcWorkbookPath`
     - Observe logs for any errors. Should any occur it's likely that the structure of the input
       spreadsheet has changed since the last run and that the conversion logic needs updating;
       it is implemented by class `GdprTransparencyConversionTask`. Most common changes experienced
       so far were in column headers and name of the tab as the task relies on them to identify
       data. 
1. **Import JSON files** using [Updater Editor] [EXIM] interface which requires admin rights.
   Configure and execute script `EximImport`:
   - set parameter `XPath query` to identify folder the parent for the folder being imported.
     If the default path currently hardcoded in `GdprImportableItem` class is used, the query
     will be:
     ```xpath
     /jcr:root/content/documents/corporate-website/about-nhs-digital[(@jcr:primaryType='hippostd:folder')]
     ``` 
   - point parameter `sourceBaseFolderPath` to the directory where the converter generated the JSON
     files into (look for `hippoImportDir` in the `Execution parameters used` summary in the
     converter's logs; unless you've changed it from command line, the location will be
     `/tmp/migrator/exim-import` by default),
   - set *Batch Size* to 1 and *Throttle* to no less than a second,
   - review the Updater Editor's log for errors.
1. **Review the imported documents** for correctness and only proceed if satisfied.
1. **Un-publish the imported documents** if you don't want them to appear as immediately published
   in the target environment.
1. **Export the temporary folder** via [Console]'s XML Export function selecting option to produce a
   single ZIP file.
1. **Import the temporary target folder** into an existing folder in the remote environment via its
   [Console] (requires admin rights):
   - Using the `XML Import` function and uploading the ZIP file generated in the previous step.
   - Specific folder doesn't matter but make sure it does not contain folder GDPR Import already
     (or to whatever you've changed it to in step 3). It can make things easier to import to the
     parent location (of `gdpr-import`) already specified in content moving script's `XPath query`
     field (see step 9).
   - The import will create a sub-folder called `GDPR Import` with the imported documents in it
     (unless you've changed it in step 3).
1. **Move content** of the temporary folder **into the target folder** by executing [Updater Editor]
   script `MoveDocumentsToFolder`:
   - if the path in XPath query field differs from that used during import in step 8, update the
     `XPath query` field to match the latter,
   - set `targetFolderPath` to point to the target folder if required,
   - see the opening comment in the script for more info.
1. Review the imported documents and publish if required.
1. Remove the now-empty, temporary folder.


[EXIM]:                         https://onehippo-forge.github.io/content-export-import/index.html
[Updater Editor]:               https://www.onehippo.org/library/concepts/update/using-the-updater-editor.html
[Console]:                      https://www.onehippo.org/library/concepts/content-repository/using-the-console.html