(:JIQS: ShouldRun; Output="(true, false)" :)
let $a := 3
let $f := function ($x) {$x = $a}
return ($f(3), $f(4))
