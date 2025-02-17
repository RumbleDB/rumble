(:JIQS: ShouldRun; Output="(0, 4)" :)
variable $x := 3,$y := $x + 1;
while ( $x gt 2 ) {
    variable $y := $x + 1;
    while ($y gt $x) {
        $y := $x;
        variable $y := 3;
        $x := $y - 3;
    }
}
($x, $y)