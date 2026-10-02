(:JIQS: ShouldRun; Output="3" :)
declare variable $result := local:f();
declare function local:f() {
    for $i in $source group by $key := $i return $key
};
declare variable $source := 3;

$result
