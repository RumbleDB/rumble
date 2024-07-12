(:JIQS: ShouldRun; Output="70" :)
variable $counter := 0;
while ( $counter lt 80 ) {
    while ( $counter lt 70) {
        $counter := $counter + 1;
    }
    break loop;
    $counter := $counter + 3;
}
$counter