(:JIQS: ShouldRun; Output="b-fn" :)
(: An import without location hints finds modules that a later import in the query provides. :)
import module namespace a = "urn:import-regression:order-a" at "order-a.jq";
import module namespace b = "urn:import-regression:order-b" at "order-b.jq";
a:f()
