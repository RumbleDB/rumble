(:JIQS: ShouldRun; Output="([ 2, 3, 4 ], [ 5, 6, 7 ])" :)
variable $je := [1 to 4], $ej := [5 to 8];
if (true) then {
    (delete json $je[[1]], delete json $ej[[4]]);
} else {();}
($je, $ej)