(:JIQS: ShouldRun; Output="3" :)
declare variable $result := local:f();
declare function local:f() {
    let $saved := $source
    let $source := 9
    return $saved
};
declare variable $source := 3;

$result
