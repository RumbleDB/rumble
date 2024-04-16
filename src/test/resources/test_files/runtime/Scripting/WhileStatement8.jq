(:JIQS: ShouldRun; Output="(2, 4)" :)
variable $x := 3,$y := 4;
while ( $x gt 2 ) {
    variable $y := $x + 3;
    while ($y gt $x) {
        variable $y := 2;
        $x := $y;
    }
}
($x, $y)