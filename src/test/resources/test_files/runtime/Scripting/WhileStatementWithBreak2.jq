(:JIQS: ShouldRun; Output="0" :)
variable $counter;
$counter := 0;
while ( $counter lt 10 ) {
    break loop;
    $counter := $counter + 1;
}
$counter