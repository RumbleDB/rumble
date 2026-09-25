(:JIQS: ShouldRun; Output="(1, 2)" :)
xquery version "3.1";
declare variable $i := local:f();
declare function local:f() {
    for tumbling window $w in (1, 2)
    start $i when $i gt 0
    end $j when $j eq $i
    return $w
};

$i
