(:JIQS: ShouldRun; Output="(2, 2)" :)
(: Inlining local:f copies its body, copy-modify included, so the copy does not share the parameter $o. :)
declare function local:f($o as object) { copy $c := $o modify replace value of json $c.a with 2 return $c.a };
(local:f({"a": 1}), local:f({"a": 3}))
