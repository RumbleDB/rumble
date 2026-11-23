(:JIQS: ShouldRun; Output="(1, 2)" :)
import schema namespace t = "urn:nested-union-cardinality" at "NestedUnionCardinality.xsd";

(: A cast to the outer union can produce a sequence, not just one atomic item. :)
let $values := "1 2" cast as t:DateOrBooleanOrIntegers
return $values
