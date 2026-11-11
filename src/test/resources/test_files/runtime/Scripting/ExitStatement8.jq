(:JIQS: ShouldRun; Output="100" :)
declare function foo($res2) {
    variable $res := $res2;
    while ($res eq 2) {
        $res := 10;
        if ($res gt 10) then {
            $res := $res + 5;
        } else {
            exit returning $res;
        }
    }
    {"a": 3}
};

declare function bar($i) {
    foo($i) * foo($i)
};

bar(2)