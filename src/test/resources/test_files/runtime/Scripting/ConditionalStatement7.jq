(:JIQS: ShouldRun; Output="(8, 10)" :)
variable $x := 3, $y := 4;
$x := $y * 2;
if ($x gt $y) then {
    variable $x := 100;
    $y := 10;
}
else {
    $x := 20;
    $y := 15;
}
($x, $y)