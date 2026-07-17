(:JIQS: ShouldRun; Output="ABC" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $constructor := function-lookup(QName("urn:example:compiled-schema", "ex:Code"), 1)
return string($constructor("ABC"))
