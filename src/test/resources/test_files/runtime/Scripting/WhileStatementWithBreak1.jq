(:JIQS: ShouldRun; Output="1" :)
variable $counter;
$counter := 0;
while ( $counter lt 10 ) {
    $counter := $counter + 1;
    break loop;
}
$counter