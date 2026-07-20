(:JIQS: ShouldCrash; ErrorCode="XQDY0084" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
validate strict { <ex:missing/> }
