(:JIQS: ShouldRun; Output="(4, -2)" :)
variable $res := 0;
variable $count := $res + 10;
while ($count gt 2) {
    switch($count)
        case 11 return { $count := -2; }
        case 4 return {
            $res := 4;
            $count := $count - 1;
            variable $count := 10;
        }
        case 3 return {$count := 11; continue loop;}
        default return $count := $count - 1;

    if ($count eq 10) then {
        break loop;
    } else ();
}
($res, $count)