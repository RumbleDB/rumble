(:JIQS: ShouldNotCompile; ErrorCode="XPST0008" :)
(: t:local is declared only inside localRoot; schema-element requires a global declaration. :)
import schema namespace t = "urn:declarations" at "Declarations.xsd";
() instance of schema-element(t:local)
