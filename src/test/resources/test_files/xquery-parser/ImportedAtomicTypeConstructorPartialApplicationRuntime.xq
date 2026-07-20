(:JIQS: ShouldRun; Output="ABC" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
string(ex:Code(?)("ABC"))
