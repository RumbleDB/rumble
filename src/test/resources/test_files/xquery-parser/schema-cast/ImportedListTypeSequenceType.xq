(:JIQS: ShouldNotCompile; ErrorCode="XPST0051" :)
import schema namespace t = "urn:cast-test" at "ImportedSimpleTypes.xsd";

(: t:TwoCodes is not a valid item type :)
let $value as t:TwoCodes := "A B" cast as t:TwoCodes
return $value
