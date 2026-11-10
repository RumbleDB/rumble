(:JIQS: ShouldRun; Output="(true, true)" :)
(: An inlined call promotes an argument to its parameter type, also when it is a variable named like the parameter. :)
declare function local:f($x as xs:double, $y as xs:double) { ($x, $y) ! (. instance of xs:double) };
let $x := 1
let $y := 2
return local:f($x, $y)
