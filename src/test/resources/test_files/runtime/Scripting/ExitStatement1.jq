(:JIQS: ShouldRun; Output="[ 1, 2, 3 ]" :)
declare variable $x := {"c": 5};
while ($x.c eq 5) {
    $x := {"c": 3};
    exit returning [1, 2, 3];
}
accumulate(({ "b" : 2 }, $x, {"c": "abc", "d" : 5e+2}))