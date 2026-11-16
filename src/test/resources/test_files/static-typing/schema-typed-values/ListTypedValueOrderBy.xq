(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
import schema namespace t = "urn:typed-values" at "TypedValues.xsd";

(: A sorting key takes one atomized value, but a t:tags element atomizes to xs:string*. Never called, so the error is static. :)
declare function local:sort() { for $t in validate { <t:tags>a b</t:tags> } order by $t return $t };
1
