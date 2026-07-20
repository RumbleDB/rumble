(:JIQS: ShouldRun; Output="(true, false)" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
("ABC" castable as ex:Code, "invalid" castable as ex:Code)
