(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : [ 3, "abc" ], "d" : 500 }" :)
declare variable $x := {"c": 5};
while ($x.c eq 5) {
    $x := {"c": 3};
}
accumulate(({ "b" : 2 }, $x, {"c": "abc", "d" : 5e+2}))