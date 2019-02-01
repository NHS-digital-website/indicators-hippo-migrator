<#-- @ftlvariable name="organisation" type="uk.nhs.digital.ps.migrator.model.hippo.Organisation" -->
{
  "name" : "${organisation.jcrNodeName}",
  "primaryType" : "website:organisation",
  "mixinTypes" : [ "mix:referenceable", "hippotaxonomy:classifiable" ],
  "properties" : [
<#if organisation.abbreviation?has_content> {
  "name" : "website:abbreviation",
  "type" : "STRING",
  "multiple" : true,
  "values" : [ "${organisation.abbreviation}" ]
},</#if>
<#if organisation.url?has_content> {
  "name" : "website:url",
  "type" : "STRING",
  "multiple" : true,
  "values" : [ "${organisation.url}" ]
},</#if>
<#if organisation.synonyms?has_content> {
  "name" : "website:synonyms",
  "type" : "STRING",
  "multiple" : true,
  "values" : [ <#list organisation.synonymsList as synonym>"${synonym?trim}"<#sep>,</#sep></#list> ]
},</#if>
<#if organisation.topics?has_content> {
  "name" : "website:topics",
  "type" : "STRING",
  "multiple" : true,
  "values" : [ <#list organisation.topicsList as topic>"${topic?trim}"<#sep>,</#sep></#list> ]
},</#if> {
    "name" : "common:searchable",
    "type" : "BOOLEAN",
    "multiple" : false,
    "values" : [ "true" ]
  }, {
    "name" : "website:shortsummary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${organisation.shortSummary}" ]
  }, {
    "name" : "website:seosummary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${organisation.seoSummary}" ]
  }, {
    "name" : "website:title",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${organisation.title}" ]
  }, {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  }, {
    "name" : "common:searchRank",
    "type" : "LONG",
    "multiple" : false,
    "values" : [ "3" ]
  }, <#if organisation.telephone?has_content>{
    "name" : "website:telephone",
    "type" : "STRING",
    "multiple" : true,
    "values" : [ "${organisation.telephone}" ]
  },</#if> {
    "name" : "hippotranslation:id",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "df40bee3-4571-49be-87b9-02d52a7cf2d4" ]
  },
<#if organisation.organisationType?has_content> {
"name" : "website:organisationType",
"type" : "STRING",
"multiple" : true,
"values" : [ "${organisation.organisationType}" ]
},</#if>
<#if organisation.email?has_content> {
"name" : "website:email",
"type" : "STRING",
"multiple" : true,
"values" : [ "${organisation.email}" ]
},</#if> {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${organisation.jcrPath}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${organisation.localizedName}" ]
  } ],
  "nodes" : [
  <#if organisation.hasAddress>{
    "name" : "website:address",
    "primaryType" : "website:address",
    "mixinTypes" : [ ],
    "properties" : [ {
      "name" : "website:location",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.location}" ]
    }, {
      "name" : "website:county",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.county}" ]
    }, {
      "name" : "website:postcode",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.postalcode}" ]
    }, {
      "name" : "website:buildingnamenumber",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.buildingName}" ]
    }, {
      "name" : "website:buildinglocation",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.buildingLocation}" ]
    }, {
      "name" : "website:country",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.country}" ]
    }, {
      "name" : "website:street",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.street}" ]
    }, {
      "name" : "website:area",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.area}" ]
    }, {
      "name" : "website:towncity",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.town}" ]
    } ],
    "nodes" : [ ]
  },</#if>
  <#if organisation.code?has_content || organisation.type?has_content>
  {
    "name" : "website:organisationcodes",
    "primaryType" : "website:organisationcode",
    "mixinTypes" : [ ],
    "properties" : [ {
      "name" : "website:code",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.code}" ]
    }, {
      "name" : "website:url",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.uri}" ]
    }, {
      "name" : "website:type",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${organisation.type}" ]
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
      "values" : [ "${organisation.summary}" ]
    } ],
    "nodes" : [ ]
  } ]
}