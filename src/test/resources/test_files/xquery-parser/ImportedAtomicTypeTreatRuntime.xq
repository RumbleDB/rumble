(:JIQS: ShouldRun; Output="ABC" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
data(validate type ex:Code { <value>ABC</value> }) treat as ex:Code
