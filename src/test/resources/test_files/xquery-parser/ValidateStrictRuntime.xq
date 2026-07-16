(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
(validate strict { <ex:root>ABC</ex:root> }) instance of element()
