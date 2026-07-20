(:JIQS: ShouldRun; Output="(true, false)" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
("ABC DEF" castable as ex:CodeList, "ABC invalid" castable as ex:CodeList)
