(:JIQS: ShouldRun; Output="5" :)
declare variable $x as string := 1;
declare variable $y as string := $x + 1;
declare variable $z as string := $x + $y + 2;

$z

