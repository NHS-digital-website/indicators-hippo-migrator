<#-- @ftlvariable name="nationalindicator" type="uk.nhs.digital.ps.migrator.model.hippo.NationalIndicator" -->
{
  "name" : "${nationalindicator.jcrNodeName}",
  "primaryType" : "nationalindicatorlibrary:indicator",
  "mixinTypes" : [ "mix:referenceable" ],
  "properties" : [ {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  }, {
    "name" : "nationalindicatorlibrary:title",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${nationalindicator.title}" ]
  }, {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${nationalindicator.jcrPath}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${nationalindicator.localizedName}" ]
  } ],
  "nodes" : [ ]
}
