(:JIQS: ShouldRun; Output="(1.5, 4.5, 4.5)" :)
import schema namespace t = "urn:typed-values" at "TypedValues.xsd";

(: Order-by and group-by keys are atomized, so a t:price element is a t:Price key. :)
(for $p in (validate { <t:price>4.5</t:price> }, validate { <t:price>1.5</t:price> })
order by $p
return data($p)),
(for $p in (validate { <t:price>4.5</t:price> }, validate { <t:price>4.5</t:price> })
group by $k := $p
return $k is statically t:Price)
