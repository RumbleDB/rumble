(:JIQS: ShouldRun; Output="2" :)
let $f := function ($x, $y) { $x + $y }
return 42=>$f(1)=>string()=>string-length()
