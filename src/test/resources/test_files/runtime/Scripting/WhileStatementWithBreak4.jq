(:JIQS: ShouldRun; Output="10" :)
variable $counter := 0;
while ( $counter lt 80 ) {
    while ( $counter lt 70) {
        $counter := $counter + 10;
        break loop;
    }
    break loop;
    $counter := $counter + 3;
}
$counter