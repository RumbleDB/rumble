(:JIQS: ShouldRun; Output="3" :)
declare variable $result := local:f();
declare function local:f() {
    for $i in $source
    let $j := $i
    where $j gt 0
    order by $j
    count $n
    return $j
};
declare variable $source := 3;

$result
