(:JIQS: ShouldCompile :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
validate type ex:Code { <ex:root>ABC</ex:root> }
