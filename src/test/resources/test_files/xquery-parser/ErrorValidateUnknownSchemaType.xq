(:JIQS: ShouldNotCompile; ErrorCode="XQST0104" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
validate type ex:Missing { <ex:root>ABC</ex:root> }
