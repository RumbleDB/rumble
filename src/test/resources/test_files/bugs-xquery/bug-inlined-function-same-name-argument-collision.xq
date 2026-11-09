(:JIQS: ShouldRun; Output="(true, 2)" :)
(: A variable named like its parameter is converted, also when a later argument reads it. :)
declare function local:f($x as xs:double, $y) { ($x instance of xs:double, $y) };
let $x := 1
return local:f($x, $x + 1)
