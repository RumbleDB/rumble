(:JIQS: ShouldRun; Output="(2, true)" :)
(: A variable named like its parameter is converted, also when an earlier argument reads it. :)
declare function local:f($x as xs:integer, $y as xs:double) { ($x, $y instance of xs:double) };
let $y := 1
return local:f($y + 1, $y)
