(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
(validate type ex:Code { <value>ABC</value> }) instance of element()
