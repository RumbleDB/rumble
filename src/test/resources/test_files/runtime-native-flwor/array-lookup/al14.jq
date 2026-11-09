(:JIQS: ShouldRun; Output="(10, 20)" :)
declare variable $k := 1;
for $i in structured-json-lines("../../../queries/difficult-names.json")
let $k := $i.indexToUse
let $c := $i.a[[$k]]
return $c