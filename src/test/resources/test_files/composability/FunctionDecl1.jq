(:JIQS: ShouldCompile :)
declare %nonsequential function foo() {
    variable $x := 3;
    while ($x eq 3) {
        if ($x eq 3) then {
            variable $y := 3;
            exit returning 5;
        } else {
            variable $x := 3;
            $x := 4;
        }
    }
 };
