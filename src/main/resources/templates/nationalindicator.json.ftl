<#-- @ftlvariable name="nationalindicator" type="uk.nhs.digital.ps.migrator.model.hippo.NationalIndicator" -->
{
  "name" : "${nationalindicator.jcrNodeName}",
  "primaryType" : "nationalindicatorlibrary:indicator",
  "mixinTypes" : [ "mix:referenceable", "hippotaxonomy:classifiable" ],
  "properties" : [ {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  }, {
    "name" : "nationalindicatorlibrary:iapCode",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${nationalindicator.iapCode}" ]
  }, {
    "name" : "nationalindicatorlibrary:title",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${nationalindicator.title}" ]
  }, {
    "name" : "nationalindicatorlibrary:publishedBy",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.publishedBy)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:reportingPeriod",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.reportingPeriod)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:reportingLevel",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.reportingLevel)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:basedOn",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.basedOn)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:contactAuthor",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.contactAuthor)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:rating",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.rating)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:assuranceDate",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.assuranceDate)!}" ]    
  }, {
    "name" : "nationalindicatorlibrary:reviewDate",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.reviewDate)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:indicatorSet",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.indicatorSet)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:purpose",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.purpose)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:definition",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.definition)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:dataSource",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.dataSource)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:numerator",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.numerator)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:denominator",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.denominator)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:calculation",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.calculation)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:methodology",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.methodology)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:interpretationGuidelines",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.interpretationGuidelines)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:caveats",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.caveats)!}" ]
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
  },<#if nationalindicator.taxonomyKeys?has_content> {
       "name" : "hippotaxonomy:keys",
       "type" : "STRING",
       "multiple" : true,
       "values" : [ "${nationalindicator.taxonomyKeys}" ]
  }</#if> ],
  "nodes" : [ ]
}
