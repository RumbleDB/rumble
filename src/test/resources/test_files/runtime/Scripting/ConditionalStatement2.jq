(:JIQS: ShouldRun; Output="([ 1, 2, 3, 4 ], [ 5, 6, 7, 8 ])" :)
variable $je := [1 to 4], $ej := [5 to 8];
if (true) then {
    variable $je := [1 to 4];
    variable $ej := [5 to 8];
    (delete json $je[[1]], delete json $ej[[4]]);
} else {();}
($je, $ej)