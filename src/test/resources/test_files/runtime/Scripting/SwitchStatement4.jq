(:JIQS: ShouldRun; Output="(4, 10)" :)
variable $res := 0;
variable $count := $res + 10;
while ($count > 2) {
    switch($count)
        case 4 return {
            $res := 4;
            $count := $count - 1;
            variable $count := 10;
        }
        case 3 return { $count := 10; break loop; }
        default return $count := $count - 1;
}
($res, $count)