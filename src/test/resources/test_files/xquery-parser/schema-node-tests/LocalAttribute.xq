(:JIQS: ShouldNotCompile; ErrorCode="XPST0008" :)
(: localAttribute is declared only inside localRoot; schema-attribute requires a global declaration. :)
import schema namespace t = "urn:declarations" at "Declarations.xsd";
() instance of schema-attribute(localAttribute)
