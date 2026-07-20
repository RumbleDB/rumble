(:JIQS: ShouldCrash; ErrorCode="XQDY0027" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
validate lax { <ex:root>invalid</ex:root> }
