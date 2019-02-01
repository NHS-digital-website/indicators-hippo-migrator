<#-- @ftlvariable name="platform" type="uk.nhs.digital.ps.migrator.model.hippo.Platform" -->
{
  "name" : "${platform.jcrNodeName}",
  "primaryType" : "website:platform",
  "mixinTypes" : [ "mix:referenceable", "hippotaxonomy:classifiable" ],
  "properties" : [ {
    "name" : "website:seosummary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platform.seoSummary}" ]
  }, <#if platform.abbreviation?has_content>{
    "name" : "website:abbreviation",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ <#list platform.abbreviationList as abbreviation>"${abbreviation?trim}"<#sep>,</#sep></#list> ]
  }, </#if>{
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  }, {
    "name" : "website:title",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platform.title}" ]
  }, <#if platform.platformUrl?has_content>{
    "name" : "website:url",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platform.platformUrl}" ]
  }, </#if>{
    "name" : "hippo:availability",
    "type" : "STRING",
    "multiple" : true,
    "values" : [ "live" ]
  }, {
    "name" : "common:searchRank",
    "type" : "LONG",
    "multiple" : false,
    "values" : [ "3" ]
  }, {
    "name" : "website:platformtype",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platform.platformType}" ]
  }, {
    "name" : "website:shortsummary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platform.shortSummary}" ]
  }, {
    "name" : "common:searchable",
    "type" : "BOOLEAN",
    "multiple" : false,
    "values" : [ "true" ]
  }, {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platform.jcrPath}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platform.localizedName}" ]
  } ],
  "nodes" : [ <#if platform.supplierUuid != ""> {
    "name" : "website:supplier",
    "primaryType" : "hippo:mirror",
    "mixinTypes" : [ ],
    "properties" : [ {
     "name" : "hippo:docbase",
     "type" : "STRING",
     "multiple" : false,
     "values" : [ "${platform.supplierUuid}" ]
    } ],
    "nodes" : [ ]
     },</#if><#if platform.resellerUuid != ""> {
    "name" : "website:reseller",
    "primaryType" : "hippo:mirror",
    "mixinTypes" : [ ],
    "properties" : [ {
    "name" : "hippo:docbase",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${platform.resellerUuid}" ]
    } ],
    "nodes" : [ ]
    },</#if> {
    "name" : "website:summary",
    "primaryType" : "hippostd:html",
    "mixinTypes" : [ ],
    "properties" : [ {
      "name" : "hippostd:content",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${platform.summary}" ]
    } ],
    "nodes" : [ ]
  }]
}