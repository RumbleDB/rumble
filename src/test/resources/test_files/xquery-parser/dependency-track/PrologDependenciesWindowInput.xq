(:JIQS: ShouldRun; Output="3" :)
xquery version "3.1";
declare variable $result := local:f();
declare function local:f() {
    for tumbling window $w in $source
    start $source when $source gt 0
    return $w
};
declare variable $source := 3;

$result
