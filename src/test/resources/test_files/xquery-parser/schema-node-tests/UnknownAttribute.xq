(:JIQS: ShouldNotCompile; ErrorCode="XPST0008" :)
(: The imported schema has no global t:missing attribute; even an empty path must raise XPST0008. :)
import schema namespace t = "urn:declarations" at "Declarations.xsd";
()/schema-attribute(t:missing)
