(:JIQS: ShouldRun; Output="(6, 4)" :)
variable $x := 3;
variable $y := 4;
while ( $x lt $y ) {
    variable $y := $x + 3;
    while ($y gt $x) {
        variable $y := 2;
        $x := $x + 3;
    }
}
($x, $y)