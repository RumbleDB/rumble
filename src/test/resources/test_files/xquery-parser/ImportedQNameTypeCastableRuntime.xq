(:JIQS: ShouldRun; Output="(true, false)" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
("ex:value" castable as ex:QNameAlias, "missing:value" castable as ex:QNameAlias)
