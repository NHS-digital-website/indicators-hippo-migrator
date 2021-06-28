<#-- @ftlvariable name="platformfolder" type="uk.nhs.digital.ps.migrator.model.hippo.PlatformFolder" -->
{
  "name" : "${platformfolder.jcrNodeName}",
  "primaryType" : "hippostd:folder",
  "mixinTypes" : [ "hippotranslation:translated", "mix:versionable", "hippo:named" ],
  "properties" : [ {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platformfolder.jcrPath}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platformfolder.localizedName}" ]
  }, {
    "name" : "hippo:name",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platformfolder.localizedName}" ]
  }, {
    "name" : "hippostd:foldertype",
    "type" : "STRING",
    "multiple" : true,
    "values" : [ "new-translated-folder", "new-document" ]
  }, {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  } ],
  "nodes" : [ ]
}
