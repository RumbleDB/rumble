(:JIQS: ShouldRun; Output="25" :)
variable $counter := 0;
while ( $counter lt 80 ) {
    $counter := $counter + 10;
    while ( $counter lt 70) {
        $counter := $counter + 10;
        while ( $counter lt 30) {
            $counter := $counter + 5;
            break loop;
        }
        break loop;
    }
    break loop;
    $counter := $counter + 3;
}
$counter