(:JIQS: ShouldNotCompile; ErrorCode="XPST0008" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
<value/>/@missing instance of schema-attribute(ex:missing)
