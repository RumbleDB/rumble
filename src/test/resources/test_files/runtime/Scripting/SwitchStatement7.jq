(:JIQS: ShouldRun; Output="exit statement!" :)
variable $res := 0;
variable $count := $res + 10;
while ($count gt 2) {
    switch($count)
        case 4 return {
            $res := 4;
            $count := $count - 1;
            variable $count := 10;
        }
        case 3 return { $count := 10;}
        default return $count := $count - 1;
    if ($count eq 10) then {
        exit returning "exit statement!";
    } else ();
}
($res, $count)