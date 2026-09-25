(:JIQS: ShouldCompile :)
(: The imported schema includes a sibling schema using a relative location. :)
import schema namespace t = "urn:test" at "SchemaImportRelativeInclude.xsd";

1 instance of t:Code
