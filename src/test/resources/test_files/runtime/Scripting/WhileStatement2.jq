(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : [ 3, "abc" ], "d" : 500 }" :)
declare %assignable variable $x := {"c": 5};
while ($x.c gt 3) {
    $x := {"c": $x.c - 1};
}
accumulate(({ "b" : 2 }, $x, {"c": "abc", "d" : 5e+2}))