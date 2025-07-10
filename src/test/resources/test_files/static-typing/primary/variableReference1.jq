(:JIQS: ShouldRun :)
declare variable $a := 12;
declare variable $b as decimal := 12;
declare variable $c := (1,3);
declare variable $d as integer? := 12;
$a is statically integer, $b is statically decimal, $c is statically integer+, $d is statically integer?