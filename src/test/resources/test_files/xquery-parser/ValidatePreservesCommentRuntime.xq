(:JIQS: ShouldRun; Output="A|preserved|BC" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $validated := validate type ex:Code { <value>A{ comment { "preserved" } }BC</value> }
return concat(
    string(($validated/node())[1]),
    "|",
    string(($validated/node())[2]),
    "|",
    string(($validated/node())[3])
)
