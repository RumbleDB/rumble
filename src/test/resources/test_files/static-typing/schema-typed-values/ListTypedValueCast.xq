(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
import schema namespace t = "urn:typed-values" at "TypedValues.xsd";

(: A cast takes one atomized value, but a t:tags element atomizes to xs:string*. Never called, so the error is static. :)
declare function local:cast() { validate { <t:tags>a b</t:tags> } cast as xs:string };
1
