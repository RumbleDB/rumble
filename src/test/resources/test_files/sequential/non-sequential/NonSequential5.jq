(:JIQS: ShouldCompile :)
variable $x := 3;
while ( $x gt 3 ) {
    variable $x := $x + 1;
    variable $y := 3;
    while ($y gt 3) {
        variable $y := 4;
    }
}