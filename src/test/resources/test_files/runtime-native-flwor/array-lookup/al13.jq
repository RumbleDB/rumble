(:JIQS: ShouldRun; Output="(10, 20)" :)
for $i in structured-json-file("../../../queries/difficult-names.json")
let $k := $i.indexToUse
let $c := $i.a[[$k]]
return $c