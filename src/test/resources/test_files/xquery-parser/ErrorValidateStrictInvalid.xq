(:JIQS: ShouldCrash; ErrorCode="XQDY0027" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
validate strict { <ex:root>invalid</ex:root> }
