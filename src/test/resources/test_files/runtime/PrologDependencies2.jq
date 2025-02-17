(:JIQS: ShouldRun; Output="6" :)
declare variable $d := $a * $b + $c + 1;
declare variable $c := $a * $b + 1;
declare variable $a := 1;
declare variable $b := $a + 1;

$d
