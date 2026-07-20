(:JIQS: ShouldRun; Output="active" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
string((validate lax { <ex:root>ABC</ex:root> })/@status)
