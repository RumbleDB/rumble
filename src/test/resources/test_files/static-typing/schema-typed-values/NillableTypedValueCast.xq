(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
import schema namespace t = "urn:typed-values" at "TypedValues.xsd";

(: A nilled t:optional-price atomizes to nothing, so only cast as t:Price? accepts it. Never called, so the error is static. :)
declare function local:cast() { validate { <t:optional-price>1</t:optional-price> } cast as t:Price };
1
