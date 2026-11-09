(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
import schema namespace t = "urn:typed-values" at "TypedValues.xsd";

(: A t:tags element atomizes to xs:string*, which an xs:string parameter does not accept. Never called, so the error is static. :)
declare function local:first($s as xs:string) { $s };
declare function local:call() { local:first(validate { <t:tags>a b</t:tags> }) };
1
