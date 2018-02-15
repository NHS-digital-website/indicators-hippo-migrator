<#-- @ftlvariable name="nationalindicator" type="uk.nhs.digital.ps.migrator.model.hippo.NationalIndicator" -->
{
  "name" : "${nationalindicator.jcrNodeName}",
<<<<<<< HEAD
  "primaryType" : "nationalindicatorlibrary:indicator",
=======
>>>>>>> [RPS-308] CI migration: better import mechanism
  "mixinTypes" : [ "mix:referenceable", "hippotaxonomy:classifiable" ],
  "properties" : [ {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
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
  }, {
    "name" : "nationalindicatorlibrary:details",
    "primaryType" : "nationalindicatorlibrary:details",
    "mixinTypes" : [ ],
    "properties" : [ {
      "name" : "nationalindicatorlibrary:caveats",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.caveats)!}" ]
    }, {
      "name" : "nationalindicatorlibrary:definition",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.definition)!}" ]
    }, {
    "name" : "nationalindicatorlibrary:interpretationGuidelines",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.interpretationGuidelines)!}" ]
    }, {
        "name" : "nationalindicatorlibrary:methodology",
        "primaryType" : "nationalindicatorlibrary:methodology",
        "mixinTypes" : [ ],
        "properties" : [ {
            "name" : "nationalindicatorlibrary:calculation",
            "type" : "STRING",
            "multiple" : false,
            "values" : [ "${(nationalindicator.calculation)!}" ]
          }, {
            "name" : "nationalindicatorlibrary:dataSource",
            "type" : "STRING",
            "multiple" : false,
            "values" : [ "${(nationalindicator.dataSource)!}" ]
          }, {
            "name" : "nationalindicatorlibrary:denominator",
            "type" : "STRING",
            "multiple" : false,
            "values" : [ "${(nationalindicator.denominator)!}" ]
          }, {
            "name" : "nationalindicatorlibrary:numerator",
            "type" : "STRING",
            "multiple" : false,
            "values" : [ "${(nationalindicator.numerator)!}" ]
          }
        ],
        "nodes" : [ ]
    }
    ],
    "nodes" : [ ]
  },{
    "name" : "nationalindicatorlibrary:purpose",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.purpose)!}" ]
  },<#if nationalindicator.taxonomyKeys?has_content> {
    "name" : "hippotaxonomy:keys",
    "type" : "STRING",
<<<<<<< HEAD
    "multiple" : false,
    "values" : [ "${(nationalindicator.calculation)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:methodology",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.methodology)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:interpretationGuidelines",
=======
    "multiple" : true,
    "values" : [ "${nationalindicator.taxonomyKeys}" ]
  } </#if>,  {
    "name" : "nationalindicatorlibrary:iapCode",
>>>>>>> [RPS-308] CI migration: better import mechanism
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${nationalindicator.iapCode}" ]
  }, {
    "name" : "nationalindicatorlibrary:indicatorSet",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.indicatorSet)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:rating",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${(nationalindicator.rating)!}" ]
  }, {
    "name" : "nationalindicatorlibrary:topbar",
    "primaryType" : "nationalindicatorlibrary:topbar",
    "mixinTypes" : [ ],
    "properties" : [  {
      "name" : "nationalindicatorlibrary:contactAuthor",
      "primaryType" : "nationalindicatorlibrary:contactAuthor",
      "mixinTypes" : [ ],
      "properties" : [ {
        "name" : "nationalindicatorlibrary:contactAuthorName",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${(nationalindicator.contactAuthorName)!}" ]
      } ],
      "nodes" : [ ]
    }, {
    "name" : "nationalindicatorlibrary:title",
    "type" : "STRING",
    "multiple" : false,
<<<<<<< HEAD
    "values" : [ "${nationalindicator.localizedName}" ]
  },<#if nationalindicator.taxonomyKeys?has_content> {
       "name" : "hippotaxonomy:keys",
       "type" : "STRING",
       "multiple" : true,
       "values" : [ "${nationalindicator.taxonomyKeys}" ]
  }</#if> ],
  "nodes" : [ ]
}
=======
    "values" : [ "${nationalindicator.title}" ]
    },{
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
      "name" : "nationalindicatorlibrary:assuranceDate",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.assuranceDate)!}" ]    
    }, {
      "name" : "nationalindicatorlibrary:reviewDate",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${(nationalindicator.reviewDate)!}" ]
    },{
      "name" : "nationalindicatorlibrary:assuredStatus",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "true" ]
    }]
  }],
    "nodes" : [ ]
  }  
>>>>>>> [RPS-308] CI migration: better import mechanism
