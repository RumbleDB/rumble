(:JIQS: ShouldRun; Output="28" :)
declare function foo() {
    variable $res := 0;
    while ($res eq 0) {
        $res := 10;
        if ($res gt 10) then {
            $res := $res + 5;
        } else {
            exit returning 28;
        }
    }
    {"a": 3}
};

declare function bar($i) {
    foo()
};

bar(2)