(:JIQS: ShouldCrash; ErrorCode="FORG0001" :)
import schema namespace t = "urn:cast-test" at "ImportedSimpleTypes.xsd";

"A B C" cast as t:TwoCodes (: violating a list’s length restriction :)
