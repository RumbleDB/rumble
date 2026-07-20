(:JIQS: ShouldRun; Output="(true, true)" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
(empty(() cast as ex:Code?), empty(() cast as ex:CodeList?))
