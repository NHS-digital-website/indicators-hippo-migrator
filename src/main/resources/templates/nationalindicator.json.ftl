<#-- @ftlvariable name="nationalindicator" type="uk.nhs.digital.ps.migrator.model.hippo.NationalIndicator" -->
{
  "name" : "${nationalindicator.jcrNodeName}",
  "primaryType" : "nationalindicatorlibrary:indicator",
  "mixinTypes" : [ "mix:referenceable", "hippotaxonomy:classifiable" ],
  "properties" : [ {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${nationalindicator.jcrPath}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${nationalindicator.localizedName}" ]
  }, {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  }, {
    "name" : "common:FacetType",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "indicator" ]
  }, {
    "name" : "hippo:availability",
    "type" : "STRING",
    "multiple" : true,
    "values" : [ ]
  }, {
    "name" : "common:searchable",
    "type" : "BOOLEAN",
    "multiple" : false,
    "values" : [ "true" ]
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
      "name" : "nationalindicatorlibrary:reportingLevel",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.reportingLevel)!}" ]
  }, {
      "name" : "nationalindicatorlibrary:assuredStatus",
      "type" : "BOOLEAN",
      "multiple" : false,
      "values" : [ "true" ]
  }, {
      "name" : "nationalindicatorlibrary:assuranceDate",
      "type" : "DATE",
      "multiple" : false,
      "values" : [ "${(nationalindicator.assuranceDate)!}" ]
  }, {
      "name" : "publicationsystem:GeographicCoverage",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.geographicCoverage)!}" ]
  }<#if nationalindicator.taxonomyKeys?has_content> ,{
      "name" : "hippotaxonomy:keys",
      "type" : "STRING",
      "multiple" : true,
      "values" : [ "${nationalindicator.taxonomyKeys}" ]
  } </#if>],
  "nodes" : [ 
    <#if nationalindicator.attachments?size != 0> <#list nationalindicator.attachments as attachment> {
    "name":"nationalindicatorlibrary:attachments",
    "primaryType" : "publicationsystem:attachment",
    "mixinTypes":[],
    "properties" : [ {
        "name" : "publicationsystem:displayName",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${attachment.title}" ]
      } ],
    "nodes" : [ {
      "name" : "publicationsystem:attachmentResource",
      "primaryType":"publicationsystem:resource",
      "mixinTypes":[],
      "properties":[ {
          "name":"hippo:filename",
          "type":"STRING",
          "multiple":false,
          "values":[ "${attachment.fileName}" ]
        }, {
          "name":"jcr:data",
          "type":"BINARY",
          "multiple":false,
          "values":[ "file://${attachment.filePathWithPlaceholder}" ]
        }, {
          "name":"jcr:mimeType",
          "type":"STRING",
          "multiple":false,
          "values":[ "${attachment.mimeType}" ]
        }, {
          "name":"jcr:lastModified",
          "type":"DATE",
          "multiple":false,
          "values":[ "0001-01-01T12:00:00.000Z" ]
        } ],
      "nodes":[]
    }]
  } <#sep>,</#sep>
  </#list>  
  ,</#if> {
    "name" : "nationalindicatorlibrary:details",
    "primaryType" : "nationalindicatorlibrary:details",
    "mixinTypes" : [ "hippotaxonomy:classifiable" ],
    "properties" : [ {
      "name" : "nationalindicatorlibrary:indicatorSet",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.indicatorSet)!}" ]
    }, {
      "name" : "nationalindicatorlibrary:briefDescription",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.briefDescription)!}" ]
    }, {
      "name" : "nationalindicatorlibrary:technicalSpecificationUrl",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.technicalSpecificationUrl)!}" ]
    }, {
      "name" : "nationalindicatorlibrary:qualityStatementUrl",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.qualityStatementUrl)!}" ]
    }, {
      "name" : "nationalindicatorlibrary:iapCode",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${nationalindicator.iapCode}" ]
    }, {
      "name" : "nationalindicatorlibrary:rating",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.rating)!}" ]
    } ],
    "nodes" : [ {
      "name" : "nationalindicatorlibrary:caveats",
      "primaryType" : "hippostd:html",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "hippostd:content",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${(nationalindicator.caveats)!}" ]
      } ],
      "nodes" : [ ]
    }, {
      "name" : "nationalindicatorlibrary:definition",
      "primaryType" : "hippostd:html",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "hippostd:content",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${(nationalindicator.definition)!}" ]
      } ],
      "nodes" : [ ]
    }, {
      "name" : "nationalindicatorlibrary:interpretationGuidelines",
      "primaryType" : "hippostd:html",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "hippostd:content",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${(nationalindicator.interpretationGuidelines)!}" ]
      } ],
      "nodes" : [ ]
    }, {
      "name" : "nationalindicatorlibrary:methodology",
      "primaryType" : "nationalindicatorlibrary:methodology",
      "mixinTypes" : [ ],
      "properties" : [ ],
      "nodes" : [ {
        "name" : "nationalindicatorlibrary:calculation",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "${(nationalindicator.calculation)!}" ]
        } ],
        "nodes" : [ ]
      }, {
        "name" : "nationalindicatorlibrary:caveats",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "${(nationalindicator.caveats)!}" ]
        } ],
        "nodes" : [ ]
      }, {
        "name" : "nationalindicatorlibrary:dataSource",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "${(nationalindicator.dataSource)!}" ]
        } ],
        "nodes" : [ ]
      }, {
        "name" : "nationalindicatorlibrary:denominator",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "${(nationalindicator.denominator)!}" ]
        } ],
        "nodes" : [ ]
      }, {
        "name" : "nationalindicatorlibrary:interpretationGuidelines",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "" ]
        } ],
        "nodes" : [ ]
      }, {
        "name" : "nationalindicatorlibrary:numerator",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "${(nationalindicator.numerator)!}" ]
        } ],
        "nodes" : [ ]
      } ]
    }, {
      "name" : "nationalindicatorlibrary:purpose",
      "primaryType" : "hippostd:html",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "hippostd:content",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${(nationalindicator.purpose)!}" ]
      } ],
      "nodes" : [ ]
    } ]
  }, {
    "name" : "nationalindicatorlibrary:topbar",
    "primaryType" : "nationalindicatorlibrary:topbar",
    "mixinTypes" : [ ],
    "properties" : [ {
      "name" : "nationalindicatorlibrary:reportingPeriod",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.reportingPeriod)!}" ]
    }, {
      "name" : "nationalindicatorlibrary:reviewDate",
      "type" : "DATE",
      "multiple" : false,
      "values" : [ "${(nationalindicator.reviewDate)!}" ]
    }, {
      "name" : "nationalindicatorlibrary:basedOn",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.basedOn)!}" ]
    } ],
    "nodes" : [ {
      "name" : "nationalindicatorlibrary:contactAuthor",
      "primaryType" : "nationalindicatorlibrary:contactAuthor",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "nationalindicatorlibrary:contactAuthorEmail",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${(nationalindicator.contactAuthorEmail)!}" ]
      }, {
        "name" : "nationalindicatorlibrary:contactAuthorName",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${(nationalindicator.contactAuthorName)!}" ]
      } ],
      "nodes" : [ ]
    } ]
  } ]
}