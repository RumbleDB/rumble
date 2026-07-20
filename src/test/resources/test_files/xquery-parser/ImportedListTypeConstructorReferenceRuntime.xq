(:JIQS: ShouldRun; Output="ABC,DEF" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $constructor := ex:CodeList#1
return string-join($constructor("ABC DEF"), ",")
