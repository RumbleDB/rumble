(:JIQS: ShouldRun; Output="(3, 2, 2)" :)
(: Predicates that are the same for every item select at most one position. :)
declare function local:f($x as xs:integer?) { $x };
let $s := (1, 2, 3)
return (local:f($s[last()]), local:f($s[2]), local:f($s[last() - 1]))
