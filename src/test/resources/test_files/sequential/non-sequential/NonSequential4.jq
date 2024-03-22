(:JIQS: ShouldCompile :)
variable $x := 3;
while ( $x gt 3 ) {
    variable $y := $x + 3;
    if ($y lt $x) then {
        $y = $x + 1;
    } else {
        $x = $y;
    }
}