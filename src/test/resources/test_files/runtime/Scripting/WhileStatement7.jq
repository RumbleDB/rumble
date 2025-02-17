(:JIQS: ShouldRun; Output="(10, 5, 6)" :)
variable $counter, $x := 5, $y;
$counter := 0;
while ( $counter lt 10 ) {
    $counter := $counter + 1;
    $y := $x + 1;
}
($counter, $x, $y)