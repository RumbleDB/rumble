(:JIQS: ShouldRun; Output="(true, true, true, true)" :)
import schema namespace t = "urn:typed-node-test" at "TypedNodeTests.xsd";

(: Function return-type covariance exercises static node subtyping, without invoking the functions. :)
let $element := function($node as element(*, t:Count)) as element(*, t:Count) { $node }
let $attribute := function($node as attribute(*, t:Count)) as attribute(*, t:Count) { $node }
return (
    $element instance of function(element(*, t:Count)) as element(*, xs:anyAtomicType),
    $element instance of function(element(*, t:Count)) as element(*, xs:numeric),
    $attribute instance of function(attribute(*, t:Count)) as attribute(*, xs:anyAtomicType),
    $attribute instance of function(attribute(*, t:Count)) as attribute(*, xs:numeric)
)
