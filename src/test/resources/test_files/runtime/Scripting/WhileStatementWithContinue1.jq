(:JIQS: ShouldRun; Output="125" :)
variable $counter := 1;
while ( $counter lt 80 ) {
    $counter := $counter * 5;
    continue loop;
    $counter := 100;
}
$counter