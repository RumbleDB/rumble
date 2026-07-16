(:JIQS: ShouldRun; Output="active" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
string((validate strict { <ex:root>ABC</ex:root> })/@status)
