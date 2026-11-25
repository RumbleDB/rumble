(:JIQS: ShouldRun; Output="(1, 2)" :)
import schema namespace t = "urn:nested-union-cardinality" at "NestedUnionCardinality.xsd";

(: The typed value is a list even though the list type is nested inside another union. :)
let $values := data(validate { <t:values>1 2</t:values> })
return $values
