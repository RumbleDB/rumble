(:JIQS: ShouldRun; Output="(1, 2)" :)
variable $x := 3,$y := 2;
while ( $x gt $y ) {
    variable $y := $x + 1;
    while ($y gt 2) {
        $y := 1;
        variable $y := 2;
        $x := $y - 1;
    }
}
($x, $y)