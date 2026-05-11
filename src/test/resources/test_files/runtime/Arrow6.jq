(:JIQS: ShouldRun; Output="4" :)
let $f := function ($x, $y) { $x + $y }
return 42=>$f(1)=>string()=>string-length()=>double()=>$f(2)
