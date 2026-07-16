(:JIQS: ShouldCrash; ErrorCode="XQDY0061" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
validate strict { document { <ex:root>ABC</ex:root>, <ex:root>ABC</ex:root> } }
