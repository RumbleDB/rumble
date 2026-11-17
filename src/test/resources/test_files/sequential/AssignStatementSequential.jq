(:JIQS: ShouldCompile :)
declare variable $x := 3;

variable $y := 2;
variable $x := 4;
if ($y > $x) then {
    $x := $x + $y;
} else {();}