<#-- @ftlvariable name="dataset" type="uk.nhs.digital.ps.migrator.model.hippo.DataSet" -->
{
  "name" : "${dataset.jcrNodeName}",
  "primaryType" : "publicationsystem:dataset",
  "mixinTypes" : [ "mix:referenceable", "hippotaxonomy:classifiable" ],
  "properties" : [ {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${dataset.jcrPath}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${dataset.localizedName}" ]
  }, {
    "name" : "publicationsystem:Title",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${dataset.title}" ]
  }, {
    "name" : "publicationsystem:Granularity",
    "type" : "STRING",
    "multiple" : true,
    "values" : [ "" ]
  }, {
    "name" : "publicationsystem:CoverageEnd",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "0001-01-01T12:00:00.000Z" ]
  }, {
    "name" : "publicationsystem:NominalDate",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "${dataset.nominalDate}" ]
  }, {
    "name" : "publicationsystem:NextPublicationDate",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "0001-01-01T12:00:00.000Z" ]
  }, {
    "name" : "publicationsystem:CoverageStart",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "0001-01-01T12:00:00.000Z" ]
  }, {
    "name" : "publicationsystem:Summary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${dataset.summary}" ]
  }, {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  } ],
  "nodes" : [
  <#list dataset.attachments as attachment>
  {
    "name":"publicationsystem:Files-v2",
    "primaryType":"publicationsystem:resource",
    "mixinTypes":[],
    "properties":[ {
        "name":"hippo:filename",
        "type":"STRING",
        "multiple":false,
        "values":[ "${attachment.title}" ]
      }, {
        "name":"jcr:data",
        "type":"BINARY",
        "multiple":false,
        "values":[ "file://${attachment.filePath}" ]
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
  } <#sep>,</#sep>
  </#list>
  ]
}
