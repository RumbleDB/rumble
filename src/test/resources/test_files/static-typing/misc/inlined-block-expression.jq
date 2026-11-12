(:JIQS: ShouldRun; Output="(3, 6)" :)
(: Inlining local:f copies its body, block included, so the copy does not share the parameter $x. :)
declare function local:f($x as integer) { $x + { $x * 2 } };
(local:f(1), local:f(2))
