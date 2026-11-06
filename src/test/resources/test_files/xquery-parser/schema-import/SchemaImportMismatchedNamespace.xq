(:JIQS: ShouldNotCompile; ErrorCode="XQST0059" :)
(: The imported schema's target namespace must match the namespace in the import declaration. :)
import schema namespace t = "urn:test" at "SchemaImportMismatchedNamespace.xsd";

1
