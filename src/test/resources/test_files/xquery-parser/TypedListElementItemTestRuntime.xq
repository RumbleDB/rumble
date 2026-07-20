(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
(validate type ex:CodeList { <value>ABC DEF</value> }) instance of element(*, ex:CodeList)
