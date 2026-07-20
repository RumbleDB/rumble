(:JIQS: ShouldRun; Output="ABC" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $constructor := ex:Code#1
return string($constructor("ABC"))
