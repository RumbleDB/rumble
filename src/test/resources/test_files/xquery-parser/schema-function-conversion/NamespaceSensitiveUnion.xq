(:JIQS: ShouldCrash; ErrorCode="XPTY0117" :)
import schema namespace t = "urn:arguments" at "Arguments.xsd";
declare function local:accept($value as t:QNameOrInteger) { $value };

(: Untyped input cannot be implicitly converted to a union containing QName. :)
let $call := local:accept#1
return $call(<value>12</value>)
