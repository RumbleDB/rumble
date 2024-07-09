(:JIQS: ShouldRun; Output="([ 2, 3, 4 ], [ 5, 6, 7 ])" :)
variable $je := [2, 3, 4];
variable $ej := [5, 6, 7];
if (true) then {
    copy json $je := [1 to 4], $ej := [5 to 8]
    modify (delete json $je[[1]], delete json $ej[[4]])
    return ($je, $ej);
} else {();}
($je, $ej)