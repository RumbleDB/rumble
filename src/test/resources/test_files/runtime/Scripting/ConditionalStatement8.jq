(:JIQS: ShouldRun; Output="(8, 10)" :)
variable $x := 3, $y := 4;
$x := $y * 2;
while ($x gt $y) {
    variable $x := 100;
    $y := 10;
    variable $y := 200;
    if ($y gt $x) then {
        $x := 15;
        break loop;
    } else {
        $x := 20;
    }
}
($x, $y)