(:JIQS: ShouldRun; Output="5" :)
declare variable $x as integer := 1;
declare variable $y as integer? := $x + 1;
declare variable $z as integer+ := $x + $y + 2;

$z

