(:JIQS: ShouldRun; Output="2" :)
let $x := 2
let $y := function() { $x }
let $x := 4
return $y()
