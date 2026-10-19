(:JIQS: ShouldRun; Output="(4.5, 3, 5)" :)
import schema namespace t = "urn:typed-values" at "TypedValues.xsd";

(: Function conversion atomizes a node argument, so its known typed value is checked statically. :)
declare function local:price($e as element(*, t:Price)?) as t:Price? { data($e) };
declare function local:add($e as element(*, xs:integer)) as xs:integer { $e + 2 };
(
    sum(validate { <t:price>4.5</t:price> }) is statically xs:decimal,
    local:price(validate { <t:amount currency="CHF">3</t:amount> }),
    local:add(validate type xs:integer { <total>3</total> })
)
