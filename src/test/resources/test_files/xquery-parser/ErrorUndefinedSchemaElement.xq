(:JIQS: ShouldNotCompile; ErrorCode="XPST0008" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
<value/> instance of schema-element(ex:missing)
