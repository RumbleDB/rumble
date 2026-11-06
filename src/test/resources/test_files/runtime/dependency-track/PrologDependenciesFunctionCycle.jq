(:JIQS: ShouldCrash; ErrorCode="XQDY0054" :)
declare variable $i := local:f();
declare function local:f() { let $i := $i return $i };

$i
