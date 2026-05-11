(:JIQS: ShouldCompile :)
declare variable $x := 3;

variable $y := 2;
variable $x := 4;
if ($y > $x) then {
    while ($x gt $y) {
        $x := $x + $y;
    }
} else {();}