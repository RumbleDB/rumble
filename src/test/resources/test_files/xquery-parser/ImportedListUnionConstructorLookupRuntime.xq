(:JIQS: ShouldRun; Output="ABC,DEF" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $constructor := function-lookup(QName("urn:example:compiled-schema", "ex:CodeListOrInteger"), 1)
return string-join($constructor("ABC DEF"), ",")
