(:JIQS: ShouldNotCompile; ErrorCode="XPST0008" :)
(: The imported schema has no global t:missing element; an empty operand does not skip static resolution. :)
import schema namespace t = "urn:declarations" at "Declarations.xsd";
() instance of schema-element(t:missing)
