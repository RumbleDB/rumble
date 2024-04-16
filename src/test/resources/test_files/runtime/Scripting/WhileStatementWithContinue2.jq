(:JIQS: ShouldRun; Output="8" :)
variable $counter := 0, $x := 5;
while ( $counter lt $x ) {
    $counter := $counter + 1;
    while ($x gt $counter) {
        $counter := $counter * 2;
        continue loop;
        $x := $x + 1;
    }
    continue loop;
    $x := $x + 1;
}
$counter