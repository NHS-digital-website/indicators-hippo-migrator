<#-- @ftlvariable name="gdprtransparencydocument" type="uk.nhs.digital.ps.migrator.model.hippo.GdprTransparencyDocument" -->
{
  "name" : "gdprtransparency",
  "primaryType" : "website:gdprtransparency",
  "mixinTypes" : [ "mix:versionable", "mix:referenceable" ],
  "properties" : [ {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.jcrPath}" ]
  }, {
    "name" : "website:outsideuk",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.outsideUk}" ]
  }, {
    "name" : "website:howuseinformation",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.howUseInformation}" ]
  }, {
    "name" : "website:computerdecision",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.computerDecision}" ]
  }, {
    "name" : "website:sensitivity",
    "type" : "BOOLEAN",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.sensitivity}" ]
  }, {
    "name" : "website:assetrefnumber",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.assetRefNumber}" ]
  }, {
    "name" : "website:title",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.title}" ]
  }, {
    "name" : "website:summary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.summary}" ]
  }, {
    "name" : "website:shortsummary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.shortSummary}" ]
  }, {
    "name" : "website:seosummary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.seoSummary}" ]
  }, {
    "name" : "website:timeretained",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.timeRetained}" ]
  }, {
    "name" : "website:datasource",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.dataSource}" ]
  }, {
    "name" : "website:rights",
    "type" : "STRING",
    "multiple" : true,
    "values" : [ "${gdprtransparencydocument.rights}" ]
  }, {
    "name" : "website:lawfulbasis",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.lawfulBasis}" ]
  }, {
    "name" : "website:datacontroller",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.dataController}" ]
  }, {
    "name" : "website:whocanaccess",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.whoCanAccess}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${gdprtransparencydocument.localizedName}" ]
  } ],
  "nodes" : [ {
    "name" : "website:legallywhy",
    "primaryType" : "hippostd:html",
    "mixinTypes" : [ ],
    "properties" : [ {
      "name" : "hippostd:content",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "<p>${gdprtransparencydocument.legallyWhy}</p>" ]
    } ],
    "nodes" : [ ]
  }, {
    "name" : "website:withdrawconsent",
    "primaryType" : "hippostd:html",
    "mixinTypes" : [ ],
    "properties" : [ {
      "name" : "hippostd:content",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "<p>${gdprtransparencydocument.withdrawConsent}</p>" ]
    } ],
    "nodes" : [ ]
  } ]
}