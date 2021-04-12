(:JIQS: ShouldRun; Output="(one, amazing)" :)
declare variable $k := "1";
for $i in structured-json-file("../../../queries/difficult-names.json")
let $k := $i.keyToUse
let $c := $i.$k
return $c
