(:JIQS: ShouldRun; Output="(1, 2)" :)
import schema namespace t = "urn:nested-union-cardinality" at "NestedUnionCardinality.xsd";

(: Binding the constructor result must preserve both values of the selected list member. :)
let $values := t:DateOrBooleanOrIntegers("1 2")
return $values
