(:JIQS: ShouldRun; Output="(1, 1, 1, 2, 3)" :)
let $i := let $j := 1 return $j return $i,
let $i := let $j := let $k := 1 return $k return $j return $i,
let $i := let $j := let $k := (1,2) let $l := ($k, 3) return $l return $j return $i

(: Test for nestedness :)
