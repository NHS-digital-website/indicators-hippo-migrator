<#-- @ftlvariable name="cyberalert" type="uk.nhs.digital.ps.migrator.model.hippo.Cyberalert" -->
{
  "name" : "${cyberalert.jcrNodeName}",
  "primaryType" : "website:cyberalert",
  "mixinTypes" : [ "mix:referenceable", "hippotaxonomy:classifiable" ],
  "properties" : [ {
        "name" : "jcr:path",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${cyberalert.jcrPath}" ]
      }, {
        "name" : "jcr:localizedName",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${cyberalert.localizedName}" ]
      }, {
       "name" : "website:severity",
       "type" : "STRING",
       "multiple" : false,
       "values" : [ "${cyberalert.threatSeverity}" ]
     }, {
       "name" : "publicationsystem:NominalDate",
       "type" : "DATE",
       "multiple" : false,
       "values" : [ "${cyberalert.datePublishedAndTime}" ]
     }, {
       "name" : "common:searchable",
       "type" : "BOOLEAN",
       "multiple" : false,
       "values" : [ "true" ]
     }, {
       "name" : "website:shortsummary",
       "type" : "STRING",
       "multiple" : false,
       "values" : [ "${cyberalert.shortSummary}" ]
     }, {
       "name" : "website:category",
       "type" : "STRING",
       "multiple" : true,
       "values" : [ ${cyberalert.threatCategory} ]
     }
    <#if cyberalert.definitiveSourceOfThreatUpdates?has_content>
    , {
       "name" : "website:sourceofthreatupdates",
       "type" : "STRING",
       "multiple" : true,
       "values" : [ "${cyberalert.definitiveSourceOfThreatUpdates}" ]
     }
    </#if>
    , {
       "name" : "website:threatid",
       "type" : "STRING",
       "multiple" : false,
       "values" : [ "${cyberalert.threatId}" ]
     }, {
       "name" : "website:publicallyaccessible",
       "type" : "BOOLEAN",
       "multiple" : false,
       "values" : [ "${cyberalert.publicallyAccessible}" ]
     }, {
       "name" : "website:threattype",
       "type" : "STRING",
       "multiple" : false,
       "values" : [ "${cyberalert.threatType}" ]
     }, {
       "name" : "hippotranslation:locale",
       "type" : "STRING",
       "multiple" : false,
       "values" : [ "en" ]
     }, {
       "name" : "website:title",
       "type" : "STRING",
       "multiple" : false,
       "values" : [ "${cyberalert.title}" ]
     }, {
       "name" : "website:threatvector",
       "type" : "STRING",
       "multiple" : true,
       "values" : [ ${cyberalert.threatVector} ]
     }, {
       "name" : "common:searchRank",
       "type" : "LONG",
       "multiple" : false,
       "values" : [ "3" ]
     }, {
       "name" : "hippotranslation:id",
       "type" : "STRING",
       "multiple" : false,
       "values" : [ "b37adba0-fb63-4ff7-8037-9964ef27e0b0" ]
     }, {
       "name" : "website:ncsclink",
       "type" : "STRING",
       "multiple" : false,
       "values" : [ "${cyberalert.linkToNcsc}" ]
     } ],
    "nodes" : [
<#if cyberalert.indicatorsOfCompromise?has_content>{
      "name" : "website:indicatorscompromisenew",
      "primaryType" : "website:expander",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "website:heading",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "Main indicators" ]
      }, {
        "name" : "website:audience",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "Most people" ]
      } ],
      "nodes" : [ {
        "name" : "website:content",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
        "name" : "hippostd:content",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${cyberalert.indicatorsOfCompromise}" ]
      } ],
      "nodes" : [ ]
      }]
    },</#if><#if cyberalert.remediationIntroduction?has_content> {
      "name" : "website:remediationintro",
      "primaryType" : "hippostd:html",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "hippostd:content",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${cyberalert.remediationIntroduction}" ]
      } ],
      "nodes" : [ ]
    },</#if><#if cyberalert.seoSummary?has_content> {
      "name" : "website:seosummary",
      "primaryType" : "hippostd:html",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "hippostd:content",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${cyberalert.seoSummary}" ]
      } ],
      "nodes" : [ ]
    },</#if>{
      "name" : "website:summary",
      "primaryType" : "hippostd:html",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "hippostd:content",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${cyberalert.summary}" ]
      } ],
      "nodes" : [ ]
    }
    <#if cyberalert.platforms?size != 0>,</#if>
    <#if cyberalert.platforms?has_content>
        <#list cyberalert.platforms as platform>
        {
          "name" : "website:threataffects",
          "primaryType" : "website:threataffect",
          "mixinTypes" : [ ],
          "properties" : [ {
            "name" : "website:versionsaffetcted",
            "type" : "STRING",
            "multiple" : true,
            "values" : [ "<#if platform?index == 0>${cyberalert.versionsAffected}</#if>" ]
          } ],
          "nodes" : [
          {
            "name" : "website:platformaffected",
            "primaryType" : "hippo:mirror",
            "mixinTypes" : [ ],
            "properties" : [ {
              "name" : "hippo:docbase",
              "type" : "STRING",
              "multiple" : false,
              "values" : [ "${platform.id}" ]
              } ],
            "nodes" : [ ]
          }
          ,
          {
            "name" : "website:platformtext",
            "primaryType" : "hippostd:html",
            "mixinTypes" : [ ],
            "properties" : [ {
              "name" : "hippostd:content",
              "type" : "STRING",
              "multiple" : false,
              "values" : [ "<#if platform?index == 0>${cyberalert.platformText}</#if>" ]
            } ],
            "nodes" : [ ]
          } ]
        } <#sep>,</#sep>
        </#list>
    <#elseif cyberalert.platformText?has_content>
        ,{
          "name" : "website:threataffects",
          "primaryType" : "website:threataffect",
          "mixinTypes" : [ ],
          "properties" : [ {
            "name" : "website:versionsaffetcted",
            "type" : "STRING",
            "multiple" : true,
            "values" : [ "${cyberalert.versionsAffected}" ]
          } ],
          "nodes" : [
          {
            "name" : "website:platformtext",
            "primaryType" : "hippostd:html",
            "mixinTypes" : [ ],
            "properties" : [ {
              "name" : "hippostd:content",
              "type" : "STRING",
              "multiple" : false,
              "values" : [ "${cyberalert.platformText}" ]
            } ],
            "nodes" : [ ]
          } ]
        }
    </#if>
    <#if cyberalert.threatDetail?has_content>, {
      "name" : "website:section",
      "primaryType" : "website:section",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "website:numberedList",
        "type" : "BOOLEAN",
        "multiple" : false,
        "values" : [ "false" ]
      }, {
        "name" : "website:title",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${cyberalert.threatHeader}" ]
      }, {
        "name" : "website:type",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "" ]
      } ],
      "nodes" : [{
        "name" : "website:html",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "${cyberalert.threatDetail}" ]
        } ],
        "nodes" : [ ]
      } ]
    }</#if>
    <#if cyberalert.updateDetail1?has_content || cyberalert.updateHeader1?has_content>
    ,{
      "name" : "website:threatupdates",
      "primaryType" : "website:threatupdate",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "website:title",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${cyberalert.updateHeader1}" ]
      }
      <#if cyberalert.updatePublished1?has_content>,
      {
        "name" : "website:date",
        "type" : "DATE",
        "multiple" : false,
        "values" : [ "${cyberalert.updatePublished1AndTime}" ]
      } </#if>],
      "nodes" : [ {
        "name" : "website:content",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "${cyberalert.updateDetail1}" ]
        } ],
        "nodes" : [ ]
      } ]
    }</#if>, {
      "name" : "website:remediationsteps",
      "primaryType" : "website:remediationstep",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "website:link",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "" ]
      }, {
        "name" : "website:type",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "" ]
      } ],
      "nodes" : [ {
        "name" : "website:step",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "${cyberalert.step}" ]
        } ],
        "nodes" : [ ]
      } ]
    }
  <#if cyberalert.cveIdentifiers?size != 0>,</#if>
  <#list cyberalert.cveIdentifiers as cve>
  {
      "name" : "website:cybercveidentifiers",
      "primaryType" : "website:cybercveidentifier",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "website:cveidentifier",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${cve}" ]
      }, {
        "name" : "website:cvestatus",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "" ]
      } ],
      "nodes" : [ {
        "name" : "website:cvetext",
        "primaryType" : "hippostd:html",
        "mixinTypes" : [ ],
        "properties" : [ {
          "name" : "hippostd:content",
          "type" : "STRING",
          "multiple" : false,
          "values" : [ "" ]
        } ],
        "nodes" : [ ]
      } ]
    }<#sep>,</#sep>
  </#list>
  ]
}